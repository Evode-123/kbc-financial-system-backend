package com.kbc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kbc.model.CashOfferingDetails;
import com.kbc.repository.CashOfferingDetailsRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CashOfferingDetailsService {

    @Autowired
    private CashOfferingDetailsRepository cashOfferingDetailsRepository;

    // Create a new cash offering detail
    public CashOfferingDetails createCashOfferingDetail(CashOfferingDetails cashOfferingDetail) {
        return cashOfferingDetailsRepository.save(cashOfferingDetail);
    }

    // Get all cash offering details
    public List<CashOfferingDetails> getAllCashOfferingDetails() {
        return cashOfferingDetailsRepository.findAll();
    }

    // Get cash offering detail by ID
    public Optional<CashOfferingDetails> getCashOfferingDetailById(UUID id) {
        return cashOfferingDetailsRepository.findById(id);
    }

    // Update an existing cash offering detail
    public CashOfferingDetails updateCashOfferingDetail(UUID id, CashOfferingDetails updatedDetail) {
        return cashOfferingDetailsRepository.findById(id).map(existingDetail -> {
            existingDetail.setCashOffering(updatedDetail.getCashOffering());
            existingDetail.setDenomination(updatedDetail.getDenomination());
            existingDetail.setQuantity(updatedDetail.getQuantity());
            existingDetail.setTotal(updatedDetail.getTotal());
            return cashOfferingDetailsRepository.save(existingDetail);
        }).orElseThrow(() -> new RuntimeException("CashOfferingDetails not found"));
    }

    // Delete a cash offering detail
    public void deleteCashOfferingDetail(UUID id) {
        cashOfferingDetailsRepository.deleteById(id);
    }

    // Get cash offering details by cash offering ID
    public List<CashOfferingDetails> getCashOfferingDetailsByCashOfferingId(UUID cashOfferingId) {
        return cashOfferingDetailsRepository.findByCashOfferingId(cashOfferingId);
    }


}
