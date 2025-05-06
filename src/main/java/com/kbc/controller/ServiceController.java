package com.kbc.controller;

import com.kbc.model.Services;
import com.kbc.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    // Create a new service
    @PostMapping("/add")
    public ResponseEntity<Services> createService(@RequestBody Services service) {
        Services createdService = serviceService.createService(service);
        return new ResponseEntity<>(createdService, HttpStatus.CREATED);
    }

    // Get all services
    @GetMapping("/get-all")
    public ResponseEntity<List<Services>> getAllServices() {
        List<Services> services = serviceService.getAllServices();
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    // Get a service by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<Services> getServiceById(@PathVariable UUID id) {
        return serviceService.getServiceById(id)
                .map(service -> new ResponseEntity<>(service, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update a service
    @PutMapping("/update/{id}")
    public ResponseEntity<Services> updateService(@PathVariable UUID id, @RequestBody Services updatedService) {
        try {
            Services service = serviceService.updateService(id, updatedService);
            return new ResponseEntity<>(service, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a service
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable UUID id) {
        try {
            serviceService.deleteService(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}