package com.kbc.service;

import com.kbc.model.AccountNo;
import com.kbc.repository.AccountNoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountNoService {

    @Autowired
    private AccountNoRepository accountNoRepository;

    // Save a new AccountNo
    public AccountNo saveNewAccountNo(AccountNo accountNo) {
        if (accountNo.getId() != null && accountNoRepository.existsById(accountNo.getId())) {
            throw new IllegalArgumentException("AccountNo with this ID already exists. Use update method instead.");
        }
        return accountNoRepository.save(accountNo);
    }

    // Update an existing AccountNo
    public AccountNo updateAccountNo(UUID id, AccountNo accountNo) {
        if (accountNo.getId() == null || !accountNo.getId().equals(id)) {
            throw new IllegalArgumentException("AccountNo ID mismatch.");
        }
        if (!accountNoRepository.existsById(id)) {
            throw new IllegalArgumentException("AccountNo with ID " + id + " does not exist.");
        }
        return accountNoRepository.save(accountNo);
    }

    // Get all AccountNos
    public List<AccountNo> getAllAccountNos() {
        return accountNoRepository.findAll();
    }

    // Get an AccountNo by ID
    public Optional<AccountNo> getAccountNoById(UUID id) {
        return accountNoRepository.findById(id);
    }

    // Delete an AccountNo by ID
    public void deleteAccountNo(UUID id) {
        accountNoRepository.deleteById(id);
    }

    public List<AccountNo> getActiveAccounts() {
        return accountNoRepository.findByStatus("ACTIVE");
    }


    // Deactivate an AccountNo
    public AccountNo deactivateAccountNo(UUID id) {
        AccountNo accountNo = accountNoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        accountNo.setStatus("INACTIVE");
        return accountNoRepository.save(accountNo);
    }

    // Reactivate an AccountNo
    public AccountNo reactivateAccountNo(UUID id) {
        AccountNo accountNo = accountNoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        accountNo.setStatus("ACTIVE");
        return accountNoRepository.save(accountNo);
    }

}
