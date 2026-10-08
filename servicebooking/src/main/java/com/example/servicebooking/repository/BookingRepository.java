package com.example.servicebooking.repository;

import com.example.servicebooking.entity.Booking;
import com.example.servicebooking.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository
        extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomerId(Long customerId);

    List<Booking> findByProviderId(Long providerId);

    List<Booking> findByStatus(BookingStatus status);
}
