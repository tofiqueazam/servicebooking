package com.example.servicebooking.dto;

import com.example.servicebooking.entity.Provider;

public record AdminProviderResponse(
        Long id,
        String name,
        String email,
        String phone,
        String city,
        Double experience,
        boolean approved
) {
    public static AdminProviderResponse from(Provider provider) {
        return new AdminProviderResponse(
                provider.getId(),
                provider.getName(),
                provider.getEmail(),
                provider.getPhone(),
                provider.getCity(),
                provider.getExperience(),
                provider.isApproved()
        );
    }
}
