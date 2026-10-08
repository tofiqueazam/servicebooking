package com.example.servicebooking.service;

import com.example.servicebooking.dto.BookingRequest;
import com.example.servicebooking.entity.*;
import com.example.servicebooking.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.servicebooking.exception.ResourceNotFoundException;


import java.util.Objects;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ProviderRepository providerRepository;
    private final ServiceRepository serviceRepository;

    public BookingService(
            BookingRepository bookingRepository,
            UserRepository userRepository,
            ProviderRepository providerRepository,
            ServiceRepository serviceRepository) {

        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.providerRepository = providerRepository;
        this.serviceRepository = serviceRepository;
    }

    @Transactional
    public Booking createBooking(Booking booking) {

        User customer = userRepository.findById(
                booking.getCustomer().getId())
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));

        Provider provider = providerRepository.findById(
                booking.getProvider().getId())
                .orElseThrow(() ->
                        new RuntimeException("Provider not found"));

        com.example.servicebooking.entity.Service service =
                serviceRepository.findById(
                        booking.getService().getId())
                        .orElseThrow(() ->
                                new RuntimeException("Service not found"));

        if (!provider.isApproved()) {
            throw new RuntimeException(
                    "Provider is not approved");
        }

        booking.setCustomer(customer);
        booking.setProvider(provider);
        booking.setService(service);
        booking.setStatus(BookingStatus.PENDING);
        booking.setTotalPrice(service.getPrice());

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));
    }

    public List<Booking> getCustomerBookings(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    public List<Booking> getProviderBookings(Long providerId) {
        return bookingRepository.findByProviderId(providerId);
    }

    @Transactional
    public Booking updateStatus(Long id, BookingStatus status) {

        Booking booking = getBookingById(id);

        if (booking.getStatus() == BookingStatus.COMPLETED
                || booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException(
                    "Booking is already in a final state");
        }

        booking.setStatus(status);

        return bookingRepository.save(booking);
    }
    @Transactional
    public Booking createBooking(String customerEmail, BookingRequest request) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Provider provider = providerRepository.findById(request.providerId())
                .orElseThrow(() -> new RuntimeException("Provider not found"));

        com.example.servicebooking.entity.Service service = serviceRepository
                .findById(request.serviceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        if (!provider.isApproved()) {
            throw new RuntimeException("Provider is not approved");
        }

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setProvider(provider);
        booking.setService(service);
        booking.setBookingDate(request.bookingDate());
        booking.setAddress(request.address());
        booking.setStatus(BookingStatus.PENDING);
        booking.setTotalPrice(service.getPrice());

        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.delete(getBookingById(id));
    }
    @Transactional
public Booking cancelBooking(Long bookingId, String customerEmail) {

    Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Booking not found"));

    // Customer sirf apni booking cancel kar sakta hai.
    if (!booking.getCustomer().getEmail().equalsIgnoreCase(customerEmail)) {
        throw new org.springframework.security.access.AccessDeniedException(
                "You cannot cancel another customer's booking");
    }

    if (booking.getStatus() != BookingStatus.PENDING
            && booking.getStatus() != BookingStatus.ACCEPTED) {
        throw new IllegalStateException(
                "Only PENDING or ACCEPTED bookings can be cancelled");
    }

    booking.setStatus(BookingStatus.CANCELLED);

    return bookingRepository.save(booking);
}
   @Transactional
public Booking updateBookingStatus(
        Long bookingId,
        BookingStatus newStatus) {

    Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Booking not found"));

    BookingStatus current = booking.getStatus();

    boolean allowed = switch (current) {
        case PENDING ->
                newStatus == BookingStatus.ACCEPTED;

        case ACCEPTED ->
                newStatus == BookingStatus.ON_THE_WAY;

        case ON_THE_WAY ->
                newStatus == BookingStatus.IN_PROGRESS;

        case IN_PROGRESS ->
                newStatus == BookingStatus.COMPLETED;

        case COMPLETED, CANCELLED -> false;
    };

    if (!allowed) {
        throw new IllegalStateException(
                "Invalid booking status transition: "
                        + current + " -> " + newStatus);
    }

    booking.setStatus(newStatus);
    return bookingRepository.save(booking);
}
}
