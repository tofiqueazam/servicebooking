package com.example.servicebooking.service;

import com.example.servicebooking.entity.Provider;
import com.example.servicebooking.repository.ProviderRepository;
import org.springframework.stereotype.Service;
import com.example.servicebooking.dto.ProviderResponse;
import com.example.servicebooking.entity.Provider;
import com.example.servicebooking.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class ProviderService {

    private final ProviderRepository providerRepository;

    public ProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    public Provider createProvider(Provider provider) {
        if (providerRepository.existsByEmail(provider.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        provider.setApproved(false);
        provider.setRating(0.0);

        return providerRepository.save(provider);
    }

    public List<Provider> getAllProviders() {
        return providerRepository.findAll();
    }

    public Provider getProviderById(Long id) {
        return providerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Provider not found"));
    }

    public List<Provider> getProvidersByCity(String city) {
        return providerRepository.findByCityIgnoreCase(city);
    }

    public List<Provider> getApprovedProviders() {
        return providerRepository.findByApprovedTrue();
    }

    public Provider updateProvider(Long id, Provider provider) {
        Provider existing = getProviderById(id);

        existing.setName(provider.getName());
        existing.setPhone(provider.getPhone());
        existing.setCity(provider.getCity());
        existing.setExperience(provider.getExperience());

        return providerRepository.save(existing);
    }

    public Provider approveProvider(Long id) {
        Provider provider = getProviderById(id);
        provider.setApproved(true);

        return providerRepository.save(provider);
    }

    public void deleteProvider(Long id) {
        providerRepository.delete(getProviderById(id));
    }
    public ProviderResponse getProviderProfile(Long providerId) {

    Provider provider = providerRepository.findById(providerId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Provider not found"));

    if (!provider.isApproved()) {
        throw new ResourceNotFoundException("Provider not found");
    }

    return ProviderResponse.from(provider);
}
}
