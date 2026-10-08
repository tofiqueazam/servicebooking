package com.example.servicebooking.service;

import com.example.servicebooking.dto.ProviderResponse;
import com.example.servicebooking.dto.ProviderSearchResponse;
import com.example.servicebooking.entity.Provider;
import com.example.servicebooking.repository.ProviderRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ProviderSearchService {

    private final ProviderRepository providerRepository;

    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of("name", "rating", "experience");

    public ProviderSearchService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    public ProviderSearchResponse searchProviders(
            String city,
            String name,
            int page,
            int size,
            String sortBy,
            String direction) {

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page number cannot be negative");
        }

        if (size < 1 || size > 50) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 50");
        }

        if (sortBy == null ||
                !ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Sort field must be name, rating, or experience");
        }

        if (direction == null ||
                !(direction.equalsIgnoreCase("asc")
                        || direction.equalsIgnoreCase("desc"))) {
            throw new IllegalArgumentException(
                    "Direction must be asc or desc");
        }

        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("asc")
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy)
        );

        Page<Provider> results =
                providerRepository.searchApprovedProviders(
                        normalize(city),
                        normalize(name),
                        pageable
                );

        List<ProviderResponse> providers = results.getContent()
                .stream()
                .map(ProviderResponse::from)
                .toList();

        return new ProviderSearchResponse(
                providers,
                results.getNumber(),
                results.getSize(),
                results.getTotalElements(),
                results.getTotalPages(),
                results.isLast()
        );
    }

    private String normalize(String value) {
        return value == null || value.isBlank()
                ? null
                : value.trim();
    }
}
