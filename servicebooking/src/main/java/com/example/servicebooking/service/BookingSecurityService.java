package com.example.servicebooking.service;

import com.example.servicebooking.entity.Booking;
import com.example.servicebooking.repository.BookingRepository;

import org.springframework.stereotype.Service;

@Service("bookingSecurityService")
public class BookingSecurityService {

    private final BookingRepository bookingRepository;

    public BookingSecurityService(
            BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public boolean isCustomerOwner(Long bookingId, String email) {
        return bookingRepository.findById(bookingId)
                .map(Booking::getCustomer)
                .map(user -> user.getEmail().equalsIgnoreCase(email))
                .orElse(false);
    }

    public boolean isProviderOwner(Long bookingId, String email) {
        return bookingRepository.findById(bookingId)
                .map(Booking::getProvider)
                .map(provider -> provider.getEmail().equalsIgnoreCase(email))
                .orElse(false);
    }
}
