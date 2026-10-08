package com.example.servicebooking.controller;

import com.example.servicebooking.dto.ReviewRequest;
import com.example.servicebooking.dto.ReviewResponse;
import com.example.servicebooking.entity.Review;
import com.example.servicebooking.service.ReviewService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/booking/{bookingId}")
    public ResponseEntity<ReviewResponse> createReview(
            @PathVariable Long bookingId,
            @Valid @RequestBody ReviewRequest request,
            Authentication authentication) {

        Review review = reviewService.createReview(
                bookingId,
                authentication.getName(),
                request
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ReviewResponse.from(review));
    }
}
