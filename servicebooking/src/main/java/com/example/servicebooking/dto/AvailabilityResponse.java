package com.example.servicebooking.dto;

import com.example.servicebooking.entity.ProviderAvailability;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record AvailabilityResponse(
        Long id,
        Long providerId,
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime
) {
    public static AvailabilityResponse from(
            ProviderAvailability availability) {

        return new AvailabilityResponse(
                availability.getId(),
                availability.getProvider().getId(),
                availability.getDayOfWeek(),
                availability.getStartTime(),
                availability.getEndTime()
        );
    }
}
