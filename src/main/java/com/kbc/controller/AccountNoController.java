package com.kbc.controller;

import com.kbc.model.AccountNo;
import com.kbc.service.AccountNoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "https://kingdom-believers-church.vercel.app")
@RequestMapping("/accountNo")
public class AccountNoController {

    @Autowired
    private AccountNoService accountNoService;

    // Create a new AccountNo
    @PostMapping("/add-account")
    public ResponseEntity<AccountNo> createAccountNo(@RequestBody AccountNo accountNo) {
        AccountNo savedAccountNo = accountNoService.saveNewAccountNo(accountNo);
        return new ResponseEntity<>(savedAccountNo, HttpStatus.CREATED);
    }

    // Update an existing AccountNo
    @PutMapping("/{id}")
    public ResponseEntity<AccountNo> updateAccountNo(@PathVariable UUID id, @RequestBody AccountNo accountNo) {
        AccountNo updatedAccountNo = accountNoService.updateAccountNo(id, accountNo);
        return new ResponseEntity<>(updatedAccountNo, HttpStatus.OK);
    }

    // Get all AccountNos
    @GetMapping("/get-all-accounts")
    public ResponseEntity<List<AccountNo>> getAllAccountNos() {
        List<AccountNo> accountNos = accountNoService.getAllAccountNos();
        return new ResponseEntity<>(accountNos, HttpStatus.OK);
    }

    // Get an AccountNo by ID
    @GetMapping("/{id}")
    public ResponseEntity<AccountNo> getAccountNoById(@PathVariable UUID id) {
        Optional<AccountNo> accountNo = accountNoService.getAccountNoById(id);
        return accountNo.map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete an AccountNo by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountNo(@PathVariable UUID id) {
        accountNoService.deleteAccountNo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Deactivate an AccountNo
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<AccountNo> deactivateAccountNo(@PathVariable UUID id) {
        AccountNo deactivatedAccountNo = accountNoService.deactivateAccountNo(id);
        return new ResponseEntity<>(deactivatedAccountNo, HttpStatus.OK);
    }

    // Reactivate an AccountNo
    @PutMapping("/{id}/reactivate")
    public ResponseEntity<AccountNo> reactivateAccountNo(@PathVariable UUID id) {
        AccountNo reactivatedAccountNo = accountNoService.reactivateAccountNo(id);
        return new ResponseEntity<>(reactivatedAccountNo, HttpStatus.OK);
    }

    @GetMapping("/active")
    public List<AccountNo> getActiveAccounts() {
        return accountNoService.getActiveAccounts();
    }

}
