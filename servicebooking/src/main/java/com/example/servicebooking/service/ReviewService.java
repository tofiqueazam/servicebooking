package com.example.servicebooking.service;

import com.example.servicebooking.dto.ReviewRequest;
import com.example.servicebooking.entity.Booking;
import com.example.servicebooking.entity.BookingStatus;
import com.example.servicebooking.entity.Provider;
import com.example.servicebooking.entity.Review;
import com.example.servicebooking.entity.User;
import com.example.servicebooking.exception.ResourceNotFoundException;
import com.example.servicebooking.repository.BookingRepository;
import com.example.servicebooking.repository.ProviderRepository;
import com.example.servicebooking.repository.ReviewRepository;
import com.example.servicebooking.repository.UserRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ProviderRepository providerRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            BookingRepository bookingRepository,
            UserRepository userRepository,
            ProviderRepository providerRepository) {
        this.reviewRepository = reviewRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.providerRepository = providerRepository;
    }

    @Transactional
    public Review createReview(
            Long bookingId,
            String customerEmail,
            ReviewRequest request) {

        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found"));

        if (!booking.getCustomer().getId().equals(customer.getId())) {
            throw new AccessDeniedException(
                    "You can review only your own booking");
        }

        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Only completed bookings can be reviewed");
        }

        if (reviewRepository.existsByBookingId(bookingId)) {
            throw new IllegalStateException(
                    "This booking already has a review");
        }

        Review review = new Review();
        review.setBooking(booking);
        review.setRating(request.rating());
        review.setComment(request.comment());
        review.setCreatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);

        // Provider ki average rating update karo.
        Provider provider = booking.getProvider();

        List<Review> reviews =
                reviewRepository.findByBookingProviderId(provider.getId());

        double average = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        provider.setRating(
                Math.round(average * 100.0) / 100.0
        );

        providerRepository.save(provider);

        return savedReview;
    }
}