package com.kbc.service;

import com.kbc.model.MomoOffering;
import com.kbc.repository.MomoOfferingRepository;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MomoOfferingService {

    @Autowired
    private MomoOfferingRepository momoOfferingRepository;

    @PrePersist
    @PreUpdate
    private void calculateTithe(MomoOffering momoOffering) {
        if (momoOffering.getAmount() > 0) {
            momoOffering.setMomoTithe(momoOffering.getAmount() * 0.1f);
        }
    }

    public float getTotalOfferings() {
        float total = momoOfferingRepository.sumAllAmounts();
        System.out.println("Total Offerings: " + total);
        return total;
    }

    public float getTotalTithe() {
        float total = momoOfferingRepository.sumAllTithes();
        System.out.println("Total Tithe: " + total);
        return total;
    }

    public float getNetAmount() {
        return getTotalOfferings() - getTotalTithe();
    }

    // Modify save method to ensure tithe is calculated
    public MomoOffering saveMomoOffering(MomoOffering momoOffering) {
        if (momoOffering.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        
        // Explicitly calculate tithe
        momoOffering.setMomoTithe(momoOffering.getAmount() * 0.1f);
        
        return momoOfferingRepository.save(momoOffering);
    }

    // Get all Momo Offerings
    public List<MomoOffering> getAllMomoOfferings() {
        List<MomoOffering> offerings = momoOfferingRepository.findAll();
        // Ensure tithe is calculated for each offering
        offerings.forEach(offering -> {
            if (offering.getMomoTithe() == null) {
                offering.setMomoTithe(offering.getAmount() * 0.1f);
            }
        });
        return offerings;
    }

    // Get a single Momo Offering by ID
    public Optional<MomoOffering> getMomoOfferingById(UUID id) {
        return momoOfferingRepository.findById(id);
    }

    // Update an existing Momo Offering
    public Optional<MomoOffering> updateMomoOffering(UUID id, MomoOffering momoOffering) {
        return momoOfferingRepository.findById(id).map(existingOffering -> {
            existingOffering.setDate(momoOffering.getDate());
            existingOffering.setServices(momoOffering.getServices());
            existingOffering.setOfferingType(momoOffering.getOfferingType());
            existingOffering.setMomoCode(momoOffering.getMomoCode());
            existingOffering.setAmount(momoOffering.getAmount()); // This triggers tithe calculation
            return momoOfferingRepository.save(existingOffering);
        });
    }

    // Delete a Momo Offering by ID
    public void deleteMomoOffering(UUID id) {
        momoOfferingRepository.deleteById(id);
    }

}