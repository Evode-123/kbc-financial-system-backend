package com.kbc.service;

import com.kbc.model.ExpensesDetails;
import com.kbc.repository.ExpensesDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExpensesDetailsService {

    @Autowired
    private ExpensesDetailsRepository expensesDetailsRepository;
    
    public ExpensesDetails saveExpenseDetail(ExpensesDetails expenseDetail) {
        return expensesDetailsRepository.save(expenseDetail);
    }
        
    public ExpensesDetails updateExpenseDetail(UUID id, ExpensesDetails updatedDetail) {
        Optional<ExpensesDetails> existingDetailOpt = expensesDetailsRepository.findById(id);
                
        if (existingDetailOpt.isPresent()) {
            ExpensesDetails existingDetail = existingDetailOpt.get();
            existingDetail.setExpenses(updatedDetail.getExpenses());
            existingDetail.setDescription(updatedDetail.getDescription());
            existingDetail.setAmount(updatedDetail.getAmount());
                    
            return expensesDetailsRepository.save(existingDetail);
        } else {
            throw new RuntimeException("Expense Detail not found with id: " + id);
        }
    }
    
    public List<ExpensesDetails> getAllExpensesDetails() {
        return expensesDetailsRepository.findAll();
    }
    
    public Optional<ExpensesDetails> getExpenseDetailById(UUID id) {
        return expensesDetailsRepository.findById(id);
    }
    
    public void deleteExpenseDetail(UUID id) {
        expensesDetailsRepository.deleteById(id);
    }
    
    public List<ExpensesDetails> getExpenseDetailsByExpenseId(UUID expenseId) {
        return expensesDetailsRepository.findByExpensesId(expenseId);
    }
}