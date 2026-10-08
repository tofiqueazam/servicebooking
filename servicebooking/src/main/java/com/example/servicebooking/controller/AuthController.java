package com.example.servicebooking.controller;

import com.example.servicebooking.dto.LoginRequest;
import com.example.servicebooking.dto.UserResponse;
import com.example.servicebooking.service.AuthService;
import com.example.servicebooking.entity.User;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request) {

        return authService.login(request)
                .<ResponseEntity<?>>map(
                        response -> ResponseEntity.ok(response))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(
                                java.util.Map.of(
                                        "message",
                                        "Invalid email or password")));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            // Public registration mein role backend control karega
            user.setRole("CUSTOMER");

            User savedUser = authService.register(user);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(java.util.Map.of(
                            "message", "Registration successful",
                            "id", savedUser.getId(),
                            "email", savedUser.getEmail()));

        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .badRequest()
                    .body(java.util.Map.of("message", ex.getMessage()));
        }
    }
}
