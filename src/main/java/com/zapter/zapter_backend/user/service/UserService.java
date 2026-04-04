package com.zapter.zapter_backend.user.service;

import com.zapter.zapter_backend.user.domain.User;
import com.zapter.zapter_backend.user.dto.user.UserResponse;
import com.zapter.zapter_backend.user.enums.Role;
import com.zapter.zapter_backend.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void addUserAddress(Long userId, String address) {
        try {
            User user = userRepository.findById(userId).get();
            user.setAddress(address);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UserResponse getUserById(Long userId) {
        try {
            User user = userRepository.findById(userId).get();
            return new UserResponse(
                    userId,
                    user.getFirstName(),
                    user.getLastName(),
                    user.getAddress()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createUserAndSetPassword(String phoneNumber, String password) {
        try {
            User user = new User();
            user.setPhoneNumber(phoneNumber);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(Role.USER);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
