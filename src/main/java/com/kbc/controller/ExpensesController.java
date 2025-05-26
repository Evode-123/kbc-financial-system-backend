package com.kbc.controller;

import com.kbc.model.Expenses;
import com.kbc.model.ExpensesDetails;
import com.kbc.service.ExpensesService;
import com.kbc.service.ExpensesDetailsService;
import com.kbc.service.ExpenseReportService;
import com.kbc.service.ExpenseReportService.ExpenseReportDTO;
import com.kbc.service.ExpenseReportService.ExpenseReportRequestDTO;
import com.kbc.service.ExpenseReportService.ExpenseReportSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "https://kingdom-believers-church.vercel.app")
@RequestMapping("/expenses")
public class ExpensesController {

    @Autowired
    private ExpensesService expensesService;
    
    @Autowired
    private ExpensesDetailsService expensesDetailsService;
    
    @Autowired
    private ExpenseReportService expenseReportService;

    // Create an Expense
    @PostMapping("/add-expense")
    public Expenses createExpense(@RequestBody Expenses expense) {
        return expensesService.saveExpense(expense);
    }

    // Update an Expense
    @PutMapping("/update-expense/{id}")
    public Expenses updateExpense(@PathVariable UUID id, @RequestBody Expenses updatedExpense) {
        return expensesService.updateExpense(id, updatedExpense);
    }

    // Get all Expenses
    @GetMapping("/get-all-expenses")
    public List<Expenses> getAllExpenses() {
        return expensesService.getAllExpenses();
    }

    // Get Expense by ID
    @GetMapping("/get-expense/{id}")
    public Optional<Expenses> getExpenseById(@PathVariable UUID id) {
        return expensesService.getExpenseById(id);
    }

    // Delete an Expense
    @DeleteMapping("/delete-expense/{id}")
    public void deleteExpense(@PathVariable UUID id) {
        expensesService.deleteExpense(id);
    }

    // Get expenses by date range
    @GetMapping("/by-date-range")
    public List<Expenses> getExpensesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return expensesService.getExpensesByDateRange(startDate, endDate);
    }

    // Get expenses by year
    @GetMapping("/by-year/{year}")
    public List<Expenses> getExpensesByYear(@PathVariable int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        return expensesService.getExpensesByDateRange(startDate, endDate);
    }

    // Get expenses by month and year
    @GetMapping("/by-month/{year}/{month}")
    public List<Expenses> getExpensesByMonth(@PathVariable int year, @PathVariable int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return expensesService.getExpensesByDateRange(startDate, endDate);
    }

    // Get expenses by cheque number
    @GetMapping("/by-cheque/{chequeNo}")
    public List<Expenses> getExpensesByChequeNo(@PathVariable String chequeNo) {
        return expensesService.getExpensesByChequeNo(chequeNo);
    }

    // Summary API for annual expenses with aggregation
    @GetMapping("/annual-summary/{year}")
    public Map<String, Object> getAnnualSummary(@PathVariable int year) {
        return expensesService.getAnnualSummary(year);
    }

    // Summary API for monthly expenses with aggregation
    @GetMapping("/monthly-summary/{year}/{month}")
    public Map<String, Object> getMonthlySummary(@PathVariable int year, @PathVariable int month) {
        return expensesService.getMonthlySummary(year, month);
    }
    
    // Expense Details Endpoints
    
    @PostMapping("/add-expense-detail")
    public ExpensesDetails createExpenseDetail(@RequestBody ExpensesDetails expenseDetail) {
        return expensesDetailsService.saveExpenseDetail(expenseDetail);
    }
    
    @PutMapping("/update-expense-detail/{id}")
    public ExpensesDetails updateExpenseDetail(@PathVariable UUID id, @RequestBody ExpensesDetails updatedDetail) {
        return expensesDetailsService.updateExpenseDetail(id, updatedDetail);
    }
    
    @GetMapping("/get-expense-details/{expenseId}")
    public List<ExpensesDetails> getExpenseDetailsByExpenseId(@PathVariable UUID expenseId) {
        return expensesDetailsService.getExpenseDetailsByExpenseId(expenseId);
    }
    
    @DeleteMapping("/delete-expense-detail/{id}")
    public void deleteExpenseDetail(@PathVariable UUID id) {
        expensesDetailsService.deleteExpenseDetail(id);
    }
    
    // Report Endpoints
    
    @PostMapping("/generate-report")
    public List<ExpenseReportDTO> generateExpenseReport(@RequestBody ExpenseReportRequestDTO requestDTO) {
        return expenseReportService.getExpenseReport(requestDTO);
    }
    
    @PostMapping("/generate-summary")
    public ExpenseReportSummaryDTO generateExpenseSummary(@RequestBody ExpenseReportRequestDTO requestDTO) {
        return expenseReportService.getExpenseSummary(requestDTO);
    }

    @GetMapping("/last-closing-balance")
    public ResponseEntity<Double> getLastClosingBalance() {
        Double lastClosingBalance = expensesService.getLastClosingBalance();
        return ResponseEntity.ok(lastClosingBalance);
    }

}