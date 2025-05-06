package com.kbc.controller;

import com.kbc.model.CashOffering;
import com.kbc.service.CashOfferingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/cash-offerings")
public class CashOfferingController {

    @Autowired
    private CashOfferingService cashOfferingService;

    @PostMapping("/add")
    public ResponseEntity<CashOffering> createCashOffering(@RequestBody CashOffering cashOffering) {
        CashOffering created = cashOfferingService.createCashOffering(cashOffering);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<CashOffering>> getAllCashOfferings() {
        List<CashOffering> offerings = cashOfferingService.getAllCashOfferings();
        return new ResponseEntity<>(offerings, HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<CashOffering> getCashOfferingById(@PathVariable UUID id) {
        Optional<CashOffering> offering = cashOfferingService.getCashOfferingById(id);
        return offering.map(ResponseEntity::ok)
                      .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CashOffering> updateCashOffering(
            @PathVariable UUID id, 
            @RequestBody CashOffering updatedOffering) {
        try {
            CashOffering updated = cashOfferingService.updateCashOffering(id, updatedOffering);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCashOffering(@PathVariable UUID id) {
        cashOfferingService.deleteCashOffering(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/totals")
    public ResponseEntity<Map<String, Float>> getTotals() {
        Map<String, Float> totals = new HashMap<>();
        totals.put("offerings", cashOfferingService.getTotalOfferings());
        totals.put("tithe", cashOfferingService.getTotalTithe());
        totals.put("net", cashOfferingService.getNetAmount());
        return ResponseEntity.ok(totals);
    }
}