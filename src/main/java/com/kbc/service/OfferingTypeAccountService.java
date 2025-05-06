package com.kbc.service;

import com.kbc.model.OfferingTypeAccount;
import com.kbc.repository.OfferingTypeAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OfferingTypeAccountService {
    
    @Autowired
    private OfferingTypeAccountRepository repository;
    
    public List<OfferingTypeAccount> getAllOfferingTypeAccounts() {
        return repository.findAll();
    }
    
    public Optional<OfferingTypeAccount> getOfferingTypeAccountById(UUID id) {
        return repository.findById(id);
    }
    
    public OfferingTypeAccount saveOfferingTypeAccount(OfferingTypeAccount offeringTypeAccount) {
        return repository.save(offeringTypeAccount);
    }
    
    public OfferingTypeAccount updateOfferingTypeAccount(UUID id, OfferingTypeAccount offeringTypeAccountDetails) {
        return repository.findById(id).map(offeringTypeAccount -> {
            offeringTypeAccount.setOfferingTypeName(offeringTypeAccountDetails.getOfferingTypeName());
            offeringTypeAccount.setAccountNo(offeringTypeAccountDetails.getAccountNo());
            offeringTypeAccount.setCurrency(offeringTypeAccountDetails.getCurrency());
            return repository.save(offeringTypeAccount);
        }).orElseThrow(() -> new RuntimeException("OfferingTypeAccount not found with id " + id));
    }
    
    public void deleteOfferingTypeAccount(UUID id) {
        repository.deleteById(id);
    }
}
