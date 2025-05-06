package com.kbc.service;

import com.kbc.model.Services;
import com.kbc.repository.ServicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ServiceService {

    @Autowired
    private ServicesRepository serviceRepository;

    // Create a new service
    public Services createService(Services service) {
        return serviceRepository.save(service);
    }

    // Get all services
    public List<Services> getAllServices() {
        return serviceRepository.findAll();
    }

    // Get a service by ID
    public Optional<Services> getServiceById(UUID id) {
        return serviceRepository.findById(id);
    }

    // Update a service
    public Services updateService(UUID id, Services updatedService) {
        if (serviceRepository.existsById(id)) {
            updatedService.setId(id); // Ensure the ID is set to the existing one
            return serviceRepository.save(updatedService);
        } else {
            throw new RuntimeException("Service not found with ID: " + id);
        }
    }

    // Delete a service
    public void deleteService(UUID id) {
        if (serviceRepository.existsById(id)) {
            serviceRepository.deleteById(id);
        } else {
            throw new RuntimeException("Service not found with ID: " + id);
        }
    }
}