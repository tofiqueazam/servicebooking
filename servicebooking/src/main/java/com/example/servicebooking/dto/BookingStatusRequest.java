package com.example.servicebooking.dto;

import com.example.servicebooking.entity.BookingStatus;
import jakarta.validation.constraints.NotNull;

public record BookingStatusRequest(
        @NotNull BookingStatus status
) {}
