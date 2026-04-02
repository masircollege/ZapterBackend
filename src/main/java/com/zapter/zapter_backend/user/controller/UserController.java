package com.zapter.zapter_backend.user.controller;

import com.zapter.zapter_backend.user.service.UserService;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zapter/user")
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping("/address/{user_id},{address}")
    public ResponseEntity<?> addAddress(@PathVariable("user_id") Long userId,@PathVariable String address) {
        try {
            userService.addUserAddress(userId,address);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
