package com.example.servicebooking.service;

import com.example.servicebooking.dto.AvailabilityRequest;
import com.example.servicebooking.entity.Provider;
import com.example.servicebooking.entity.ProviderAvailability;
import com.example.servicebooking.exception.ResourceNotFoundException;
import com.example.servicebooking.repository.ProviderAvailabilityRepository;
import com.example.servicebooking.repository.ProviderRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProviderAvailabilityService {

    private final ProviderAvailabilityRepository availabilityRepository;
    private final ProviderRepository providerRepository;

    public ProviderAvailabilityService(
            ProviderAvailabilityRepository availabilityRepository,
            ProviderRepository providerRepository) {
        this.availabilityRepository = availabilityRepository;
        this.providerRepository = providerRepository;
    }

    @Transactional
    public ProviderAvailability addAvailability(
            Long providerId,
            String authenticatedEmail,
            AvailabilityRequest request) {

        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Provider not found"));

        if (!provider.getEmail().equalsIgnoreCase(authenticatedEmail)) {
            throw new AccessDeniedException(
                    "You can update only your own availability");
        }

        if (!provider.isApproved()) {
            throw new AccessDeniedException(
                    "Provider must be approved first");
        }

        if (!request.startTime().isBefore(request.endTime())) {
            throw new IllegalArgumentException(
                    "Start time must be before end time");
        }

        List<ProviderAvailability> existing =
                availabilityRepository.findByProviderIdAndDayOfWeek(
                        providerId, request.dayOfWeek());

        boolean overlaps = existing.stream().anyMatch(slot ->
                request.startTime().isBefore(slot.getEndTime())
                        && request.endTime().isAfter(slot.getStartTime())
        );

        if (overlaps) {
            throw new IllegalArgumentException(
                    "Availability overlaps an existing time slot");
        }

        ProviderAvailability availability = new ProviderAvailability();
        availability.setProvider(provider);
        availability.setDayOfWeek(request.dayOfWeek());
        availability.setStartTime(request.startTime());
        availability.setEndTime(request.endTime());

        return availabilityRepository.save(availability);
    }

    @Transactional(readOnly = true)
    public List<ProviderAvailability> getAvailability(Long providerId) {

        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Provider not found"));

        if (!provider.isApproved()) {
            throw new ResourceNotFoundException("Provider not found");
        }

        return availabilityRepository.findByProviderId(providerId);
    }
}
