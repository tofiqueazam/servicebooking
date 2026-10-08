package com.example.servicebooking.controller;

import com.example.servicebooking.dto.ProviderSearchResponse;
import com.example.servicebooking.service.ProviderSearchService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/providers")
public class ProviderSearchController {

    private final ProviderSearchService searchService;

    public ProviderSearchController(
            ProviderSearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public ResponseEntity<ProviderSearchResponse> searchProviders(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "rating") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        return ResponseEntity.ok(
                searchService.searchProviders(
                        city, name, page, size, sortBy, direction)
        );
    }
}
