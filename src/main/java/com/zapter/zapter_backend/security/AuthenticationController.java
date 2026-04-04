package com.zapter.zapter_backend.security;

import com.zapter.zapter_backend.security.dto.UserInputPhone;
import com.zapter.zapter_backend.user.dto.admin.AdminLogin;
import com.zapter.zapter_backend.user.dto.user.NewUser;
import com.zapter.zapter_backend.user.dto.user.UserLogin;
import com.zapter.zapter_backend.user.enums.Role;
import com.zapter.zapter_backend.user.service.AdminService;
import com.zapter.zapter_backend.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zapter")
public class AuthenticationController {
	
	private final AuthenticationService authService;
	private final AdminService adminService;
	private final UserService userService;

	public AuthenticationController(
			AuthenticationService authService,
			AdminService adminService,
			UserService userService)
	{
		this.authService = authService;
		this.adminService = adminService;
		this.userService = userService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserLogin user) {
		try{
			String token = authService.login(user);
			return new ResponseEntity<>(token,HttpStatus.OK);
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody UserInputPhone inputPhone) {
			if (authService.signup(inputPhone)){
				return new ResponseEntity<>(HttpStatus.CREATED);
			}
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

	@PostMapping("/signup/otp/verify")
	public ResponseEntity<String> verifyOtp(@Valid @RequestBody UserInputPhone inputPhone, String otp) {
		try {
			if (authService.verifyOtp(inputPhone.phoneNumber(),otp)){
				return new ResponseEntity<>(HttpStatus.OK);
			}
			return new ResponseEntity<>("Invalid OTP!",HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@PostMapping("/signup/user/set-password/{password}&{confirm-password}")
	public ResponseEntity<String> setPassword(@Valid @RequestBody UserInputPhone inputPhone,@PathVariable String password, @PathVariable("confirm-password")  String confirmPassword) {
		try {
			if (password.equals(confirmPassword)) {
				userService.createUserAndSetPassword(inputPhone.phoneNumber(),password);
				return new ResponseEntity<>(HttpStatus.OK);
			}
			return new ResponseEntity<>("Passwords don't match!",HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@PostMapping("/emp-admin-login")
	public ResponseEntity<String> adminLogin(@Valid @RequestBody AdminLogin adminLogin) {
        try {
            	String token = authService.login(adminLogin);
                return new ResponseEntity<>(token, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	@PostMapping("/emp-admin-register")
	public ResponseEntity<?> adminRegister(@Valid @RequestParam Long employeeId) {
		try {
			String role = authService.employeeExist(employeeId);
				if (role.equals("SUPER_ADMIN")) {
					adminService.createAdmin(employeeId, Role.SUPER_ADMIN);
				}
				else if (role.equals("SYSTEM_ADMIN")) {
					adminService.createAdmin(employeeId, Role.SYSTEM_ADMIN);
					}
				else {
					adminService.createAdmin(employeeId, Role.INVENTORY_ADMIN);
				}
					return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
	/*
	@PostMapping("/admin/register")
	public ResponseEntity<?> adminSetPassword(@Valid @RequestBody SetAdminPassword password) {
			if (authService.employeeExist(employeeId)){
				return new ResponseEntity<>(HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }*/


	/*
	@PostMapping("/seller/register")
	public ResponseEntity<?> sellerSignup(@Valid @RequestBody NewUser newUser) {
			if (authService.signup(newUser)){
				return new ResponseEntity<>(HttpStatus.CREATED);
			}
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
*/
