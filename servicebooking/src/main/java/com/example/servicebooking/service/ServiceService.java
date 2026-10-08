package com.example.servicebooking.service;

import com.example.servicebooking.entity.Service;
import com.example.servicebooking.repository.ServiceRepository;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public Service create(Service service) {
        return serviceRepository.save(service);
    }

    public List<Service> getAll() {
        return serviceRepository.findAll();
    }

    public Service getById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
    }

    public List<Service> getByCategory(Long categoryId) {
        return serviceRepository.findByCategoryId(categoryId);
    }

    public Service update(Long id, Service service) {
        Service existing = getById(id);

        existing.setName(service.getName());
        existing.setDescription(service.getDescription());
        existing.setPrice(service.getPrice());
        existing.setCategory(service.getCategory());

        return serviceRepository.save(existing);
    }

    public void delete(Long id) {
        serviceRepository.delete(getById(id));
    }
}
