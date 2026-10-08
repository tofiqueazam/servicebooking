package com.example.servicebooking.controller;

import com.example.servicebooking.entity.Booking;
import com.example.servicebooking.entity.BookingStatus;
import com.example.servicebooking.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.servicebooking.dto.BookingRequest;
import com.example.servicebooking.dto.BookingResponse;


import jakarta.validation.Valid;


import org.springframework.security.core.Authentication;

import com.example.servicebooking.dto.BookingStatusRequest;



import java.util.List;


@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
     @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody BookingRequest request,
            Authentication authentication) {

        String customerEmail = authentication.getName();

        Booking booking = bookingService.createBooking(
                customerEmail,
                request
        );

        BookingResponse response = BookingResponse.from(booking);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or " +
            "@bookingSecurityService.isCustomerOwner(#id, authentication.name) or " +
            "@bookingSecurityService.isProviderOwner(#id, authentication.name)")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Booking>> getCustomerBookings(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(bookingService.getCustomerBookings(customerId));
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<Booking>> getProviderBookings(
            @PathVariable Long providerId) {
        return ResponseEntity.ok(bookingService.getProviderBookings(providerId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/cancel")
@PreAuthorize("hasRole('ADMIN') or " +
        "@bookingSecurityService.isCustomerOwner(#p0, authentication.name)")
public ResponseEntity<BookingResponse> cancelBooking(
        @PathVariable Long id,
        Authentication authentication) {

    Booking booking = bookingService.cancelBooking(
            id, authentication.getName());

    return ResponseEntity.ok(BookingResponse.from(booking));
}
@PatchMapping("/{id}/status")
@PreAuthorize("hasRole('ADMIN') or " +
        "@bookingSecurityService.isProviderOwner(#p0, authentication.name)")
public ResponseEntity<BookingResponse> updateStatus(
        @PathVariable Long id,
        @Valid @RequestBody BookingStatusRequest request) {

    Booking booking = bookingService.updateBookingStatus(
            id, request.status());

    return ResponseEntity.ok(BookingResponse.from(booking));
}
}
