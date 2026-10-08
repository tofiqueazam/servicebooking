package com.example.servicebooking.security;

import com.example.servicebooking.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service("userSecurityService")
public class UserSecurityService {

    private final UserRepository userRepository;

    public UserSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isCurrentUser(Long userId, String email) {
        return userRepository.findById(userId)
                .map(user -> user.getEmail().equalsIgnoreCase(email))
                .orElse(false);
    }
}
