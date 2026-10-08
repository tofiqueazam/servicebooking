package com.example.servicebooking.repository;

import com.example.servicebooking.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository
        extends JpaRepository<Review, Long> {

    List<Review> findByBookingProviderId(Long providerId);

    boolean existsByBookingId(Long bookingId);
}
