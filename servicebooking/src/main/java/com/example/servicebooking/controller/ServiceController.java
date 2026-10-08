package com.example.servicebooking.controller;

import com.example.servicebooking.entity.Service;
import com.example.servicebooking.service.ServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @PostMapping
    public ResponseEntity<Service> create(@RequestBody Service service) {
        return ResponseEntity.ok(serviceService.create(service));
    }

    @GetMapping
    public ResponseEntity<List<Service>> getAll() {
        return ResponseEntity.ok(serviceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Service> getById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.getById(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Service>> getByCategory(
            @PathVariable Long categoryId) {

        return ResponseEntity.ok(
                serviceService.getByCategory(categoryId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Service> update(
            @PathVariable Long id,
            @RequestBody Service service) {

        return ResponseEntity.ok(serviceService.update(id, service));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        serviceService.delete(id);
        return ResponseEntity.ok("Service deleted successfully");
    }
}
