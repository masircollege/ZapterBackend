package com.zapter.zapter_backend.security;

import com.zapter.zapter_backend.security.dto.UserInputPhone;
import com.zapter.zapter_backend.user.domain.User;
import com.zapter.zapter_backend.user.dto.admin.AdminLogin;
import com.zapter.zapter_backend.user.dto.user.NewUser;
import com.zapter.zapter_backend.user.dto.user.UserLogin;
import com.zapter.zapter_backend.user.interfaces.Accounts;
import com.zapter.zapter_backend.user.mapper.EmployeeMapper;
import com.zapter.zapter_backend.user.mapper.UserMapper;
import com.zapter.zapter_backend.user.repository.AdminRepository;
import com.zapter.zapter_backend.user.repository.EmployeeRepository;
import com.zapter.zapter_backend.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

	private final UserRepository userRepository;
	private final EmployeeRepository employeeRepository;
	private final AdminRepository adminRepository;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;
	private final EmployeeMapper employeeMapper;

	@Value("${twilio.account-sid}")
	private String accountSid;

	@Value("${twilio.auth-token}")
	private String authToken;

	@Value("${twilio.verify-service-sid}")
	private String verifyServiceSid;

	@PostConstruct
	public void init() {
		Twilio.init(accountSid, authToken);
	}

	public AuthenticationService(
			UserRepository userRepository,
			EmployeeRepository employeeRepository,
			AdminRepository adminRepository,
			JwtService jwtService,
			AuthenticationManager authenticationManager, 
			PasswordEncoder passwordEncoder,
			UserMapper userMapper,
			EmployeeMapper employeeMapper
			) {
		this.userRepository = userRepository;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.userMapper = userMapper;
		this.employeeRepository = employeeRepository;
		this.employeeMapper = employeeMapper;
		this.adminRepository = adminRepository;
	}

	public boolean signup(UserInputPhone inputPhone) {
        try {
            if (inputPhone != null){
				if (userRepository.findByPhoneNumber(inputPhone.phoneNumber()).isPresent()) {
					return false;
				}

				sendOtp(inputPhone.phoneNumber());
                return true;
            }
			return false;
        } catch (Exception e) {
            return false;
        }
	}

	public void sendOtp(String toPhone) {
		try {
			Verification.creator(verifyServiceSid, toPhone, "sms").create();
		} catch (ApiException e) {
			throw new RuntimeException("Failed to send OTP to " + maskPhone(toPhone) + ": " + e.getMessage());
		}
	}

	public boolean verifyOtp(String toPhone, String otp) {
		try {
			VerificationCheck check = VerificationCheck.creator(verifyServiceSid)
					.setTo(toPhone)
					.setCode(otp)
					.create();
			return "approved".equals(check.getStatus());
		} catch (ApiException e) {
			// Twilio throws 404 if OTP not found / already used
			return false;
		}
	}

	private String maskPhone(String phone) {
		if (phone == null || phone.length() <= 4) return "****";
		return phone.substring(0, phone.length() - 4).replaceAll("\\d", "*")
				+ phone.substring(phone.length() - 4);
	}

	public String login(Accounts account) {
		try {
			if (account instanceof UserLogin user){
//				UserLogin user = (UserLogin) account; I was doing typecasting whereas it could be done by "pattern variable".
				authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(user.phoneNumber(), user.password())
					);
				return jwtService.generateToken(userRepository.findByPhoneNumber(user.phoneNumber()).orElseThrow());
			} else if (account instanceof AdminLogin admin) {
				authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(admin.adminId(), admin.password())
				);
				return jwtService.generateToken(adminRepository.findByAdminId(admin.adminId()).orElseThrow());
			}

			return  "";
	} catch (AuthenticationException e) {
		throw new RuntimeException(e);
		}
	}

	public String employeeExist(Long employeeId){
        return employeeRepository.ifEmployeeExistsGetRole(employeeId);
		// Below is another way to do the same check whether the employee is Present :
		/* 		if (employee.isPresent()){
			return true;
		} else {
			return false;
		} */
	}
}

