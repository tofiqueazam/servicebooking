package com.example.servicebooking.dto;

public record LoginResponse(
        String token,
        String tokenType,
        UserResponse user
) {
}
