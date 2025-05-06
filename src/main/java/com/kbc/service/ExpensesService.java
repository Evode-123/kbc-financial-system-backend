package com.kbc.service;

import com.kbc.model.Expenses;
import com.kbc.repository.ExpensesRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExpensesService {

    @Autowired
    private ExpensesRepository expensesRepository;
    
    public Expenses saveExpense(Expenses expense) {
        // When saving a new expense, set its opening balance to the last record's closing balance
        if (expense.getId() == null) {  // This is a new expense, not an update
            Double lastClosingBalance = getLastClosingBalance();
            expense.setOpeningBalance(lastClosingBalance);
            
            // Recalculate closing balance based on the new opening balance
            double closingBalance = expense.getOpeningBalance() + expense.getAmountWithdrawn()- expense.getTotalAmount();
            expense.setClosingBalance(closingBalance);
        }
        
        return expensesRepository.save(expense);
    }

    public List<Expenses> getAllExpenses() {
        return expensesRepository.findAll();
    }

    public Optional<Expenses> getExpenseById(UUID id) {
        return expensesRepository.findById(id);
    }

    @Transactional
public Expenses updateExpense(UUID id, Expenses updatedExpense) {
    // Get all expenses ordered by date and creation time
    List<Expenses> allExpenses = expensesRepository.findAll(Sort.by("date").ascending()
            .and(Sort.by("createdAt").ascending()));
    
    // Find the expense to update and its position
    Expenses expenseToUpdate = null;
    int updateIndex = -1;
    for (int i = 0; i < allExpenses.size(); i++) {
        if (allExpenses.get(i).getId().equals(id)) {
            expenseToUpdate = allExpenses.get(i);
            updateIndex = i;
            break;
        }
    }
    
    if (expenseToUpdate == null) {
        return null; // Expense not found
    }
    
    // Store old values for comparison
    double oldOpeningBalance = expenseToUpdate.getOpeningBalance();
    double oldClosingBalance = expenseToUpdate.getClosingBalance();
    
    // Update the expense fields (except opening balance if it's not the first)
    if (updateIndex > 0) {
        // For non-first expenses, keep the original opening balance
        updatedExpense.setOpeningBalance(oldOpeningBalance);
    } else {
        // For first expense, allow opening balance change
        expenseToUpdate.setOpeningBalance(updatedExpense.getOpeningBalance());
    }
    
    expenseToUpdate.setDate(updatedExpense.getDate());
    expenseToUpdate.setAccountNo(updatedExpense.getAccountNo());
    expenseToUpdate.setChequeNo(updatedExpense.getChequeNo());
    expenseToUpdate.setAmountWithdrawn(updatedExpense.getAmountWithdrawn());
    expenseToUpdate.setTotalAmount(updatedExpense.getTotalAmount());
    
    // Recalculate closing balance
    double newClosingBalance = expenseToUpdate.getOpeningBalance() + 
                             expenseToUpdate.getAmountWithdrawn() - 
                             expenseToUpdate.getTotalAmount();
    expenseToUpdate.setClosingBalance(newClosingBalance);
    
    // Save the updated expense
    Expenses savedExpense = expensesRepository.save(expenseToUpdate);
    
    // If balances changed, update subsequent records
    if (newClosingBalance != oldClosingBalance || 
        expenseToUpdate.getOpeningBalance() != oldOpeningBalance) {
        updateSubsequentExpenses(allExpenses, updateIndex, newClosingBalance);
    }
    
    return savedExpense;
}

private void updateSubsequentExpenses(List<Expenses> allExpenses, int startIndex, double newOpeningBalance) {
    double currentBalance = newOpeningBalance;
    
    // Create new list for updates to avoid concurrent modification
    List<Expenses> toUpdate = new ArrayList<>();
    
    // Start from the next expense after our updated one
    for (int i = startIndex + 1; i < allExpenses.size(); i++) {
        Expenses current = allExpenses.get(i);
        current.setOpeningBalance(currentBalance);
        double newClosing = currentBalance + 
                          current.getAmountWithdrawn() - 
                          current.getTotalAmount();
        current.setClosingBalance(newClosing);
        currentBalance = newClosing;
        toUpdate.add(current);
    }
    
    // Batch update all subsequent expenses
    if (!toUpdate.isEmpty()) {
        expensesRepository.saveAll(toUpdate);
    }
}

    @Transactional
public void deleteExpense(UUID id) {
    // First get all expenses in order
    List<Expenses> allExpenses = expensesRepository.findAll(Sort.by("date").ascending()
        .and(Sort.by("createdAt").ascending()));
    
    // Find the expense to delete and its position
    Expenses expenseToDelete = null;
    int deleteIndex = -1;
    for (int i = 0; i < allExpenses.size(); i++) {
        if (allExpenses.get(i).getId().equals(id)) {
            expenseToDelete = allExpenses.get(i);
            deleteIndex = i;
            break;
        }
    }
    
    if (expenseToDelete == null) {
        return; // Expense not found
    }
    
    // Calculate the new opening balance for subsequent expenses
    double newOpeningBalance = (deleteIndex > 0) 
        ? allExpenses.get(deleteIndex - 1).getClosingBalance() 
        : 0.0;
    
    // Delete the expense first
    expensesRepository.deleteById(id);
    
    // Create a new list for subsequent expenses (avoid modifying the original list)
    List<Expenses> subsequentExpenses = new ArrayList<>();
    for (int i = deleteIndex; i < allExpenses.size(); i++) {
        if (!allExpenses.get(i).getId().equals(id)) { // Skip the deleted expense
            subsequentExpenses.add(allExpenses.get(i));
        }
    }
    
    // Update balances for subsequent expenses
    double currentBalance = newOpeningBalance;
    for (Expenses expense : subsequentExpenses) {
        expense.setOpeningBalance(currentBalance);
        double newClosingBalance = currentBalance + 
                                expense.getAmountWithdrawn() - 
                                expense.getTotalAmount();
        expense.setClosingBalance(newClosingBalance);
        currentBalance = newClosingBalance;
    }
    
    // Save all updated expenses
    if (!subsequentExpenses.isEmpty()) {
        expensesRepository.saveAll(subsequentExpenses);
    }
}
    
    // Find expenses by date range
    public List<Expenses> getExpensesByDateRange(LocalDate startDate, LocalDate endDate) {
        return expensesRepository.findByDateBetween(startDate, endDate);
    }
    
    // Find expenses by cheque number
    public List<Expenses> getExpensesByChequeNo(String chequeNo) {
        return expensesRepository.findByChequeNo(chequeNo);
    }
    
    // Get annual summary with aggregated data
    public Map<String, Object> getAnnualSummary(int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        
        List<Expenses> yearlyExpenses = getExpensesByDateRange(startDate, endDate);
        
        // Calculate total expenses for the year
        double totalExpensesAmount = yearlyExpenses.stream()
                .map(Expenses::getTotalAmount)
                .reduce(0.0, Double::sum);
        
        // Calculate total withdrawals for the year
        double totalWithdrawn = yearlyExpenses.stream()
                .map(Expenses::getAmountWithdrawn)
                .reduce(0.0, Double::sum);
        
        // Group expenses by month
        Map<Integer, Double> expensesByMonth = yearlyExpenses.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getDate().getMonthValue(),
                        Collectors.summingDouble(e -> e.getTotalAmount())
                ));
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("year", year);
        summary.put("totalExpenses", totalExpensesAmount);
        summary.put("totalWithdrawn", totalWithdrawn);
        summary.put("monthlyBreakdown", expensesByMonth);
        summary.put("expenseCount", yearlyExpenses.size());
        
        return summary;
    }
    
    // Get monthly summary with aggregated data
    public Map<String, Object> getMonthlySummary(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        
        List<Expenses> monthlyExpenses = getExpensesByDateRange(startDate, endDate);
        
        // Calculate total expenses for the month
        double totalExpensesAmount = monthlyExpenses.stream()
                .map(Expenses::getTotalAmount)
                .reduce(0.0, Double::sum);
        
        // Calculate total withdrawals for the month
        double totalWithdrawn = monthlyExpenses.stream()
                .map(Expenses::getAmountWithdrawn)
                .reduce(0.0, Double::sum);
        
        // Get account-wise expenses
        Map<String, Double> expensesByAccount = monthlyExpenses.stream()
            .filter(e -> e.getAccountNo() != null)
            .collect(Collectors.groupingBy(
                    e -> e.getAccountNo().toString(), // Convert AccountNo to String
                    Collectors.summingDouble(Expenses::getTotalAmount)
            ));
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("year", year);
        summary.put("month", month);
        summary.put("totalExpenses", totalExpensesAmount);
        summary.put("totalWithdrawn", totalWithdrawn);
        summary.put("expenseCount", monthlyExpenses.size());
        summary.put("accountBreakdown", expensesByAccount);
        
        return summary;
    }

    // In ExpensesService.java
    public Double getLastClosingBalance() {
        return expensesRepository.findTopByOrderByDateDescCreatedAtDesc()
            .map(Expenses::getClosingBalance)
            .orElse(0.0);
    }


} 