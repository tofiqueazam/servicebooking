package com.example.servicebooking.dto;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record BookingRequest(

        @NotNull(message = "Provider ID is required")
        @Positive(message = "Provider ID must be positive")
        Long providerId,

        @NotNull(message = "Service ID is required")
        @Positive(message = "Service ID must be positive")
        Long serviceId,

        @NotNull(message = "Booking date is required")
        @Future(message = "Booking date must be in the future")
        LocalDateTime bookingDate,

        @NotBlank(message = "Address is required")
        String address

) {
}
