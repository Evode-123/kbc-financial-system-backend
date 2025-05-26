package com.kbc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.kbc.model.CashOfferingDetails;
import com.kbc.service.CashOfferingDetailsService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "https://kingdom-believers-church.vercel.app")
@RequestMapping("/cash-offering-details")
public class CashOfferingDetailsController {

    @Autowired
    private CashOfferingDetailsService cashOfferingDetailsService;

    @PostMapping("/add")
    public CashOfferingDetails createCashOfferingDetail(@RequestBody CashOfferingDetails cashOfferingDetail) {
        return cashOfferingDetailsService.createCashOfferingDetail(cashOfferingDetail);
    }

    @GetMapping("/get-all")
    public List<CashOfferingDetails> getAllCashOfferingDetails() {
        return cashOfferingDetailsService.getAllCashOfferingDetails();
    }

    @GetMapping("/get-by/{id}")
    public Optional<CashOfferingDetails> getCashOfferingDetailById(@PathVariable UUID id) {
        return cashOfferingDetailsService.getCashOfferingDetailById(id);
    }

    @PutMapping("/update-by/{id}")
    public CashOfferingDetails updateCashOfferingDetail(@PathVariable UUID id, @RequestBody CashOfferingDetails updatedDetail) {
        return cashOfferingDetailsService.updateCashOfferingDetail(id, updatedDetail);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCashOfferingDetail(@PathVariable UUID id) {
        cashOfferingDetailsService.deleteCashOfferingDetail(id);
    }

    @GetMapping("/by-cash-offering/{cashOfferingId}")
    public List<CashOfferingDetails> getCashOfferingDetailsByCashOfferingId(@PathVariable UUID cashOfferingId) {
        return cashOfferingDetailsService.getCashOfferingDetailsByCashOfferingId(cashOfferingId);
    }

}
