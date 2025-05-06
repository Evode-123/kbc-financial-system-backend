package com.kbc.service;

import com.kbc.model.CashOffering;
import com.kbc.repository.CashOfferingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CashOfferingService {

    @Autowired
    private CashOfferingRepository cashOfferingRepository;

    public float getTotalOfferings() {
        return cashOfferingRepository.sumAllAmounts();
    }

    public float getTotalTithe() {
        return cashOfferingRepository.sumAllTithes();
    }

    public float getNetAmount() {
        return getTotalOfferings() - getTotalTithe();
    }

    // Create a new cash offering
    public CashOffering createCashOffering(CashOffering cashOffering) {
        if (cashOffering.getTotalAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        return cashOfferingRepository.save(cashOffering);
    }

    // Get all cash offerings
    public List<CashOffering> getAllCashOfferings() {
        List<CashOffering> offerings = cashOfferingRepository.findAll();
        offerings.forEach(offering -> {
            if (offering.getCashTithe() == null) {
                offering.setCashTithe(offering.getTotalAmount() * 0.1f);
            }
        });
        return offerings;
    }

    // Get cash offering by ID
    public Optional<CashOffering> getCashOfferingById(UUID id) {
        return cashOfferingRepository.findById(id);
    }

    // Update an existing cash offering
    public CashOffering updateCashOffering(UUID id, CashOffering updatedOffering) {
        return cashOfferingRepository.findById(id).map(existingOffering -> {
            existingOffering.setDate(updatedOffering.getDate());
            existingOffering.setServices(updatedOffering.getServices());
            existingOffering.setOfferingType(updatedOffering.getOfferingType());
            existingOffering.setAccountNo(updatedOffering.getAccountNo());
            existingOffering.setCurrency(updatedOffering.getCurrency());
            existingOffering.setAmount(updatedOffering.getTotalAmount()); // Triggers tithe calculation
            return cashOfferingRepository.save(existingOffering);
        }).orElseThrow(() -> new RuntimeException("CashOffering not found"));
    }

    // Delete a cash offering
    public void deleteCashOffering(UUID id) {
        cashOfferingRepository.deleteById(id);
    }
}