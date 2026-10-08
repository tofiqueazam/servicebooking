package com.example.servicebooking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequest(

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be 2 to 50 characters")
    String name,

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email")
    String email,

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100,
          message = "Password must be at least 8 characters")
    String password,

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$",
             message = "Phone number must contain exactly 10 digits")
    String phone

) {}
