package com.example.servicebooking.service;

import com.example.servicebooking.dto.AdminProviderResponse;
import com.example.servicebooking.entity.Provider;
import com.example.servicebooking.exception.ResourceNotFoundException;
import com.example.servicebooking.repository.ProviderRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminProviderService {

    private final ProviderRepository providerRepository;

    public AdminProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    @Transactional(readOnly = true)
    public List<AdminProviderResponse> getPendingProviders() {
        return providerRepository.findByApprovedFalse()
                .stream()
                .map(AdminProviderResponse::from)
                .toList();
    }

    @Transactional
    public AdminProviderResponse approveProvider(Long providerId) {
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Provider not found"));

        provider.setApproved(true);

        Provider saved = providerRepository.save(provider);
        return AdminProviderResponse.from(saved);
    }

    @Transactional
    public AdminProviderResponse rejectProvider(Long providerId) {
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Provider not found"));

        provider.setApproved(false);

        Provider saved = providerRepository.save(provider);
        return AdminProviderResponse.from(saved);
    }
}
