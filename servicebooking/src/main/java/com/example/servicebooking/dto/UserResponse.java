package com.example.servicebooking.dto;

public record UserResponse(
        Long id,
        String name,
        String email,
        String phone,
        String role
) {
}
