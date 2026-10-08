package com.example.servicebooking.dto;

import com.example.servicebooking.entity.Provider;

public record ProviderResponse(
        Long id,
        String name,
        String phone,
        String city,
        Double experience,
        Double rating
) {
    public static ProviderResponse from(Provider provider) {
        return new ProviderResponse(
                provider.getId(),
                provider.getName(),
                provider.getPhone(),
                provider.getCity(),
                provider.getExperience(),
                provider.getRating()
        );
    }
}
