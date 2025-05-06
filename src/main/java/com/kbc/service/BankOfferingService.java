package com.kbc.service;

import com.kbc.model.BankOffering;
import com.kbc.repository.BankOfferingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class BankOfferingService {

    @Autowired
    private BankOfferingRepository bankOfferingRepository;

    public float getTotalOfferings() {
        return bankOfferingRepository.sumAllAmounts();
    }

    public float getTotalTithe() {
        return bankOfferingRepository.sumAllTithes();
    }

    public float getNetAmount() {
        return getTotalOfferings() - getTotalTithe();
    }

    // Create a new BankOffering
    public BankOffering createBankOffering(BankOffering bankOffering) {
        if (bankOffering.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        return bankOfferingRepository.save(bankOffering);
    }

    // Add this method to handle multiple BankOfferings
    public List<BankOffering> createMultipleBankOfferings(List<BankOffering> bankOfferings) {
        // Validate all offerings first
        for (BankOffering offering : bankOfferings) {
            if (offering.getAmount() <= 0) {
                throw new IllegalArgumentException("All amounts must be positive");
            }
        }
        return bankOfferingRepository.saveAll(bankOfferings);
    }
    // Read a BankOffering by ID
    public Optional<BankOffering> getBankOfferingById(UUID id) {
        return bankOfferingRepository.findById(id);
    }

    // Update an existing BankOffering
    public BankOffering updateBankOffering(UUID id, BankOffering bankOfferingDetails) {
        return bankOfferingRepository.findById(id).map(existingOffering -> {
            existingOffering.setDate(bankOfferingDetails.getDate());
            existingOffering.setServices(bankOfferingDetails.getServices());
            existingOffering.setOfferingType(bankOfferingDetails.getOfferingType());
            existingOffering.setAccountNo(bankOfferingDetails.getAccountNo());
            existingOffering.setAmount(bankOfferingDetails.getAmount()); // Triggers tithe calculation
            return bankOfferingRepository.save(existingOffering);
        }).orElseThrow(() -> new RuntimeException("BankOffering not found with ID: " + id));
    }

    // Delete a BankOffering by ID
    public void deleteBankOffering(UUID id) {
        bankOfferingRepository.deleteById(id);
    }

    // Get all BankOfferings
    public Iterable<BankOffering> getAllBankOfferings() {
        Iterable<BankOffering> offerings = bankOfferingRepository.findAll();
        offerings.forEach(offering -> {
            if (offering.getBankTithe() == null) {
                offering.setBankTithe(offering.getAmount() * 0.1f);
            }
        });
        return offerings;
    }
}