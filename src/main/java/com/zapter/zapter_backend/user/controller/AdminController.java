package com.zapter.zapter_backend.user.controller;

import com.zapter.zapter_backend.user.service.AdminService;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zapter/admin")
public class AdminController {
    final private AdminService adminService;

    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }

    @PatchMapping("/set-password")
    public ResponseEntity<?> setPassword(
            @RequestParam String id,
            @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^+-=&*])[a-zA-Z0-9!@#$%+-=^&*]{8,}$", message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
            String password
    ){
        try {
            adminService.setPassword(id, password);
            return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
