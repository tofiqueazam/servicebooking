package com.example.servicebooking.controller;

import com.example.servicebooking.dto.AdminProviderResponse;
import com.example.servicebooking.service.AdminProviderService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/providers")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProviderController {

    private final AdminProviderService adminProviderService;

    public AdminProviderController(
            AdminProviderService adminProviderService) {
        this.adminProviderService = adminProviderService;
    }

    @GetMapping("/pending")
    public ResponseEntity<List<AdminProviderResponse>> getPendingProviders() {
        return ResponseEntity.ok(
                adminProviderService.getPendingProviders()
        );
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<AdminProviderResponse> approveProvider(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                adminProviderService.approveProvider(id)
        );
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<AdminProviderResponse> rejectProvider(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                adminProviderService.rejectProvider(id)
        );
    }
}
