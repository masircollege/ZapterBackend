package com.zapter.zapter_backend.user.service;

import com.zapter.zapter_backend.user.domain.User;
import com.zapter.zapter_backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

}
