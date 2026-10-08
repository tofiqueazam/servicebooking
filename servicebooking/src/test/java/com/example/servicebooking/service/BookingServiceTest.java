package com.example.servicebooking.service;

import com.example.servicebooking.dto.BookingRequest;
import com.example.servicebooking.entity.*;
import com.example.servicebooking.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void createBookingShouldSavePendingBooking() {
        // Arrange: test data taiyar karo
        User customer = new User();
        customer.setId(1L);
        customer.setEmail("customer@example.com");

        Provider provider = new Provider();
        provider.setId(2L);
        provider.setApproved(true);

        com.example.servicebooking.entity.Service service =
                new com.example.servicebooking.entity.Service();
        service.setId(3L);
        service.setPrice(500.0);

        BookingRequest request = new BookingRequest(
                2L,
                3L,
                LocalDateTime.now().plusDays(1),
                "Ranchi"
        );

        when(userRepository.findByEmail("customer@example.com"))
                .thenReturn(Optional.of(customer));

        when(providerRepository.findById(2L))
                .thenReturn(Optional.of(provider));

        when(serviceRepository.findById(3L))
                .thenReturn(Optional.of(service));

        // Act: method execute karo
        // Assert: booking save hone ka verification
        when(bookingRepository.save(any(Booking.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Booking result = bookingService.createBooking(
                "customer@example.com", request);

        // Verify
        assertEquals(BookingStatus.PENDING, result.getStatus());
        assertEquals(500.0, result.getTotalPrice());
        assertEquals(customer, result.getCustomer());
        assertEquals(provider, result.getProvider());

        verify(bookingRepository).save(any(Booking.class));
    }
} 
