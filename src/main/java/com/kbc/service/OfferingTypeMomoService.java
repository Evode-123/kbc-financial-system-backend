package com.kbc.service;

import com.kbc.model.OfferingTypeMomo;
import com.kbc.repository.OfferingTypeMomoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OfferingTypeMomoService {
    
    @Autowired
    private OfferingTypeMomoRepository repository;
    
    public List<OfferingTypeMomo> getAllOfferingTypeMomos() {
        return repository.findAll();
    }
    
    public Optional<OfferingTypeMomo> getOfferingTypeMomoById(UUID id) {
        return repository.findById(id);
    }
    
    public OfferingTypeMomo saveOfferingTypeMomo(OfferingTypeMomo offeringTypeMomo) {
        return repository.save(offeringTypeMomo);
    }
    
    public OfferingTypeMomo updateOfferingTypeMomo(UUID id, OfferingTypeMomo offeringTypeMomoDetails) {
        return repository.findById(id).map(offeringTypeMomo -> {
            offeringTypeMomo.setOfferingTypeName(offeringTypeMomoDetails.getOfferingTypeName());
            offeringTypeMomo.setMomoCode(offeringTypeMomoDetails.getMomoCode());
            return repository.save(offeringTypeMomo);
        }).orElseThrow(() -> new RuntimeException("OfferingTypeMomo not found with id " + id));
    }
    
    public void deleteOfferingTypeMomo(UUID id) {
        repository.deleteById(id);
    }

    // Method to get momoCode based on OfferingTypeMomo ID
    public Optional<String> getMomoCodeByOfferingTypeMomoId(UUID id) {
        return repository.findMomoCodeByOfferingTypeMomoId(id);
    }

}
