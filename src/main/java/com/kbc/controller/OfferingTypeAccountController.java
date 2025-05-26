package com.kbc.controller;

import com.kbc.model.OfferingTypeAccount;
import com.kbc.service.OfferingTypeAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "https://kingdom-believers-church.vercel.app")
@RequestMapping("/offering-type-accounts")
public class OfferingTypeAccountController {

    @Autowired
    private OfferingTypeAccountService service;

    @GetMapping("/get-all-offeringtype-accounts")
    public List<OfferingTypeAccount> getAllOfferingTypeAccounts() {
        return service.getAllOfferingTypeAccounts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferingTypeAccount> getOfferingTypeAccountById(@PathVariable UUID id) {
        Optional<OfferingTypeAccount> offeringTypeAccount = service.getOfferingTypeAccountById(id);
        return offeringTypeAccount.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("add-offeringtype-account")
    public OfferingTypeAccount createOfferingTypeAccount(@RequestBody OfferingTypeAccount offeringTypeAccount) {
        return service.saveOfferingTypeAccount(offeringTypeAccount);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfferingTypeAccount> updateOfferingTypeAccount(@PathVariable UUID id, @RequestBody OfferingTypeAccount offeringTypeAccountDetails) {
        try {
            OfferingTypeAccount updatedOfferingTypeAccount = service.updateOfferingTypeAccount(id, offeringTypeAccountDetails);
            return ResponseEntity.ok(updatedOfferingTypeAccount);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOfferingTypeAccount(@PathVariable UUID id) {
        service.deleteOfferingTypeAccount(id);
        return ResponseEntity.noContent().build();
    }
}
