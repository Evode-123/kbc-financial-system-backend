package com.kbc.service;

import com.kbc.model.MomoCode;
import com.kbc.repository.MomoCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MomoCodeService {

    @Autowired
    private MomoCodeRepository momoCodeRepository;

    // Save a new MomoCode
    public MomoCode saveNewMomoCode(MomoCode momoCode) {
        if (momoCode.getId() != null && momoCodeRepository.existsById(momoCode.getId())) {
            throw new IllegalArgumentException("MomoCode with this ID already exists. Use update method instead.");
        }
        return momoCodeRepository.save(momoCode);
    }

    // Update an existing MomoCode
    public MomoCode updateMomoCode(UUID id, MomoCode momoCode) {
        if (momoCode.getId() == null || !momoCode.getId().equals(id)) {
            throw new IllegalArgumentException("MomoCode ID mismatch.");
        }
        if (!momoCodeRepository.existsById(id)) {
            throw new IllegalArgumentException("MomoCode with ID " + id + " does not exist.");
        }
        return momoCodeRepository.save(momoCode);
    }

    // Get all MomoCodes
    public List<MomoCode> getAllMomoCodes() {
        return momoCodeRepository.findAll();
    }

    // Get a MomoCode by ID
    public Optional<MomoCode> getMomoCodeById(UUID id) {
        return momoCodeRepository.findById(id);
    }

    // Delete a MomoCode by ID
    public void deleteMomoCode(UUID id) {
        momoCodeRepository.deleteById(id);
    }

    public List<MomoCode> getActiveMomoCodes() {
        return momoCodeRepository.findByStatus("ACTIVE");
    }
        // Deactivate an AccountNo
    public MomoCode deactivateMomoCode(UUID id) {
        MomoCode momo = momoCodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        momo.setStatus("INACTIVE");
        return momoCodeRepository.save(momo);
    }

    // Reactivate an AccountNo
    public MomoCode reactivateMomoCode(UUID id) {
        MomoCode momo = momoCodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        momo.setStatus("ACTIVE");
        return momoCodeRepository.save(momo);
    }

}
