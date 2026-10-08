package com.example.servicebooking.dto;

import com.example.servicebooking.entity.Review;
import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Long bookingId,
        Long providerId,
        String customerName,
        Integer rating,
        String comment,
        LocalDateTime createdAt
) {
    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getBooking().getId(),
                review.getBooking().getProvider().getId(),
                review.getBooking().getCustomer().getName(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}