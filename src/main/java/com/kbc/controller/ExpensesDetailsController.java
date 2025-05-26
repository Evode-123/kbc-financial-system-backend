package com.kbc.controller;

import com.kbc.model.ExpensesDetails;
import com.kbc.service.ExpensesDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "https://kingdom-believers-church.vercel.app")
@RequestMapping("/expense-details")
public class ExpensesDetailsController {

    @Autowired
    private ExpensesDetailsService expensesDetailsService;

    @PostMapping("/add-expense-detail")
    public ResponseEntity<ExpensesDetails> createExpenseDetail(@RequestBody ExpensesDetails expenseDetail) {
        ExpensesDetails savedDetail = expensesDetailsService.saveExpenseDetail(expenseDetail);
        return ResponseEntity.ok(savedDetail);
    }
    
    @PutMapping("/update-expense-details/{id}")
    public ExpensesDetails updateExpenseDetail(@PathVariable UUID id, @RequestBody ExpensesDetails updatedDetail) {
        return expensesDetailsService.updateExpenseDetail(id, updatedDetail);
    }

    @GetMapping("/get-all-expense-details")
    public List<ExpensesDetails> getAllExpensesDetails() {
        return expensesDetailsService.getAllExpensesDetails();
    }

    @GetMapping("/get-all-expense-detail/{id}")
    public Optional<ExpensesDetails> getExpenseDetailById(@PathVariable UUID id) {
        return expensesDetailsService.getExpenseDetailById(id);
    }

    @DeleteMapping("/delete-expense-detail/{id}")
    public void deleteExpenseDetail(@PathVariable UUID id) {
        expensesDetailsService.deleteExpenseDetail(id);
    }

    // Add this endpoint to get all expense details for a specific expense
    @GetMapping("/by-expense/{expenseId}")
    public List<ExpensesDetails> getExpenseDetailsByExpenseId(@PathVariable UUID expenseId) {
        return expensesDetailsService.getExpenseDetailsByExpenseId(expenseId);
    }

}