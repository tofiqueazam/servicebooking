package com.example.servicebooking.service;

import com.example.servicebooking.entity.User;
import com.example.servicebooking.exception.ResourceNotFoundException;
import com.example.servicebooking.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor injection
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 1. Create User
    @Transactional
    public User createUser(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException(
                    "Email is already registered"
            );
        }

        // Hash password before saving
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        return userRepository.save(user);
    }

    // 2. Get All Users
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 3. Get User By ID
    @Transactional(readOnly = true)
    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );
    }

    // 4. Update User
    @Transactional
    public User updateUser(Long id, User userDetails) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );

        // Check whether email belongs to another user
        userRepository.findByEmail(userDetails.getEmail())
                .filter(existingUser ->
                        !existingUser.getId().equals(id))
                .ifPresent(existingUser -> {
                    throw new IllegalArgumentException(
                            "Email is already registered"
                    );
                });

        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setPhone(userDetails.getPhone());

        // Hash the new password before updating
        user.setPassword(
                passwordEncoder.encode(userDetails.getPassword())
        );

        // Role is intentionally not updated from request
        return userRepository.save(user);
    }

    // 5. Delete User
    @Transactional
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );

        userRepository.delete(user);
    }
}



