package com.example.servicebooking.dto;

import java.util.List;

public record ProviderSearchResponse(
        List<ProviderResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last
) {}
