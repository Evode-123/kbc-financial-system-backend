package com.kbc.controller;

import com.kbc.model.BankOffering;
import com.kbc.service.BankOfferingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "https://kingdom-believers-church.vercel.app")
@RequestMapping("/bank-offerings")
public class BankOfferingController {

    @Autowired
    private BankOfferingService bankOfferingService;

    // Create a new BankOffering
    @PostMapping("/add")
    public ResponseEntity<BankOffering> createBankOffering(@RequestBody BankOffering bankOffering) {
        BankOffering createdBankOffering = bankOfferingService.createBankOffering(bankOffering);
        return new ResponseEntity<>(createdBankOffering, HttpStatus.CREATED);
    }

    // Add this new endpoint to handle multiple BankOfferings
    @PostMapping("/add-multiple")
    public ResponseEntity<List<BankOffering>> createMultipleBankOfferings(@RequestBody List<BankOffering> bankOfferings) {
        List<BankOffering> createdBankOfferings = bankOfferingService.createMultipleBankOfferings(bankOfferings);
        return new ResponseEntity<>(createdBankOfferings, HttpStatus.CREATED);
    }
    // Get a BankOffering by ID
    @GetMapping("/get-by/{id}")
    public ResponseEntity<BankOffering> getBankOfferingById(@PathVariable UUID id) {
        return bankOfferingService.getBankOfferingById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update a BankOffering
    @PutMapping("/update-by/{id}")
    public ResponseEntity<BankOffering> updateBankOffering(@PathVariable UUID id, @RequestBody BankOffering bankOfferingDetails) {
        try {
            BankOffering updatedBankOffering = bankOfferingService.updateBankOffering(id, bankOfferingDetails);
            return new ResponseEntity<>(updatedBankOffering, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a BankOffering
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteBankOffering(@PathVariable UUID id) {
        bankOfferingService.deleteBankOffering(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get all BankOfferings
    @GetMapping("/get-all-bank-offerings")
    public ResponseEntity<Iterable<BankOffering>> getAllBankOfferings() {
        Iterable<BankOffering> bankOfferings = bankOfferingService.getAllBankOfferings();
        return new ResponseEntity<>(bankOfferings, HttpStatus.OK);
    }

    // Get totals
    @GetMapping("/totals")
    public ResponseEntity<Map<String, Float>> getTotals() {
        Map<String, Float> totals = new HashMap<>();
        totals.put("offerings", bankOfferingService.getTotalOfferings());
        totals.put("tithe", bankOfferingService.getTotalTithe());
        totals.put("net", bankOfferingService.getNetAmount());
        return ResponseEntity.ok(totals);
    }
}