package com.example.servicebooking.dto;

import com.example.servicebooking.entity.Booking;
import com.example.servicebooking.entity.BookingStatus;

import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        Long customerId,
        String customerName,
        Long providerId,
        String providerName,
        Long serviceId,
        String serviceName,
        LocalDateTime bookingDate,
        BookingStatus status,
        String address,
        Double totalPrice
) {

    public static BookingResponse from(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getCustomer().getId(),
                booking.getCustomer().getName(),
                booking.getProvider().getId(),
                booking.getProvider().getName(),
                booking.getService().getId(),
                booking.getService().getName(),
                booking.getBookingDate(),
                booking.getStatus(),
                booking.getAddress(),
                booking.getTotalPrice()
        );
    }
}
