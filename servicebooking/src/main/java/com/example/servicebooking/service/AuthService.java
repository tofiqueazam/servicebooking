package com.example.servicebooking.service;

import com.example.servicebooking.dto.LoginRequest;
import com.example.servicebooking.dto.LoginResponse;
import com.example.servicebooking.dto.UserResponse;
import com.example.servicebooking.entity.User;
import com.example.servicebooking.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Optional<LoginResponse> login(LoginRequest request) {

        Optional<User> userOptional =
                userRepository.findByEmail(request.email());

        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        User user = userOptional.get();

        boolean passwordMatches = passwordEncoder.matches(
                request.password(),
                user.getPassword()
        );

        if (!passwordMatches) {
            return Optional.empty();
        }

        String token = jwtService.generateToken(user.getEmail());

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );

        return Optional.of(new LoginResponse(token, "Bearer", userResponse));
    }
       public User register(User user) {
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        throw new IllegalArgumentException("Email already registered");
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    if (user.getRole() == null || user.getRole().isBlank()) {
        user.setRole("CUSTOMER");
    }

    return userRepository.save(user);
}
}
