package com.example.servicebooking.security;

import com.example.servicebooking.repository.ProviderRepository;

import org.springframework.stereotype.Service;

@Service("providerSecurityService")
public class ProviderSecurityService {

    private final ProviderRepository providerRepository;

    public ProviderSecurityService(
            ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    public boolean isCurrentProvider(Long providerId, String email) {
        return providerRepository.findById(providerId)
                .map(provider ->
                    provider.getEmail().equalsIgnoreCase(email))
                .orElse(false);
    }
}
