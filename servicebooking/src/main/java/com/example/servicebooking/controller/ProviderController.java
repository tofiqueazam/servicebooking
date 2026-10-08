package com.example.servicebooking.controller;

import com.example.servicebooking.dto.ProviderResponse;
import com.example.servicebooking.entity.Provider;
import com.example.servicebooking.service.ProviderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.servicebooking.dto.AvailabilityRequest;
import com.example.servicebooking.dto.AvailabilityResponse;
import com.example.servicebooking.entity.ProviderAvailability;
import com.example.servicebooking.service.ProviderAvailabilityService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;



import java.util.List;

@RestController
@RequestMapping("/api/providers")
public class ProviderController {

    private final ProviderService providerService;

    public ProviderController(
            ProviderService providerService,
            ProviderAvailabilityService availabilityService) {
        this.providerService = providerService;
        this.availabilityService = availabilityService;
    }
    private final ProviderAvailabilityService availabilityService;

    @PostMapping
    public ResponseEntity<Provider> createProvider(
            @RequestBody Provider provider) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(providerService.createProvider(provider));
    }

    @GetMapping
    public ResponseEntity<List<Provider>> getAllProviders() {
        return ResponseEntity.ok(
                providerService.getAllProviders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Provider> getProviderById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                providerService.getProviderById(id));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<Provider>> getByCity(
            @PathVariable String city) {

        return ResponseEntity.ok(
                providerService.getProvidersByCity(city));
    }

    @GetMapping("/approved")
    public ResponseEntity<List<Provider>> getApprovedProviders() {
        return ResponseEntity.ok(
                providerService.getApprovedProviders());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Provider> updateProvider(
            @PathVariable Long id,
            @RequestBody Provider provider) {

        return ResponseEntity.ok(
                providerService.updateProvider(id, provider));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvider(
            @PathVariable Long id) {

        providerService.deleteProvider(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Provider> approveProvider(
            @PathVariable Long id) {

        return ResponseEntity.ok(providerService.approveProvider(id));
    }
    @GetMapping("/{id}/profile")
public ResponseEntity<ProviderResponse> getProviderProfile(
        @PathVariable Long id) {

    return ResponseEntity.ok(
            providerService.getProviderProfile(id)
    );
}

    @PostMapping("/{providerId}/availability")
public ResponseEntity<AvailabilityResponse> addAvailability(
        @PathVariable Long providerId,
        @Valid @RequestBody AvailabilityRequest request,
        Authentication authentication) {

    ProviderAvailability saved = availabilityService.addAvailability(
            providerId,
            authentication.getName(),
            request
    );

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(AvailabilityResponse.from(saved));
}
@GetMapping("/{providerId}/availability")
public ResponseEntity<List<AvailabilityResponse>> getAvailability(
        @PathVariable Long providerId) {

    List<AvailabilityResponse> response =
            availabilityService.getAvailability(providerId)
                    .stream()
                    .map(AvailabilityResponse::from)
                    .toList();

    return ResponseEntity.ok(response);
}
}
