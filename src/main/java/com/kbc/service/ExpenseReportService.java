package com.kbc.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kbc.model.Expenses;
import com.kbc.model.ExpensesDetails;
import com.kbc.repository.ExpensesRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
public class ExpenseReportService {

    @Autowired
    private ExpensesRepository expensesRepository;

    // Updated DTOs to match the new model structure
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpenseReportDTO {
        private UUID id;
        private LocalDate date;
        private String accountNo;
        private String chequeNo;
        private double openingBalance;
        private double amountWithdrawn;
        private double totalAmount;
        private double closingBalance;
        private List<ExpenseDetailDTO> details;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpenseDetailDTO {
        private String description;
        private double amount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpenseReportSummaryDTO {
        private LocalDate startDate;
        private LocalDate endDate;
        private double totalWithdrawn;
        private double totalExpenses;
        private int transactionCount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpenseReportRequestDTO {
        private String reportType; // MONTHLY, YEARLY, CUSTOM
        private LocalDate startDate; // For CUSTOM type
        private LocalDate endDate; // For CUSTOM type
        private Integer month; // For monthly reports (1-12)
        private Integer year;  // For yearly and monthly reports
    }

    public List<ExpenseReportDTO> getExpenseReport(ExpenseReportRequestDTO requestDTO) {
        LocalDate startDate, endDate;
        LocalDate today = LocalDate.now();
        
        switch (requestDTO.getReportType().toUpperCase()) {
            case "MONTHLY":
                // Handle selected month (default to current month if not specified)
                int month = requestDTO.getMonth() != null ? requestDTO.getMonth() : today.getMonthValue();
                int year = requestDTO.getYear() != null ? requestDTO.getYear() : today.getYear();
                
                startDate = LocalDate.of(year, month, 1);
                endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
                break;
                
            case "YEARLY":
                // Handle selected year (default to current year if not specified)
                year = requestDTO.getYear() != null ? requestDTO.getYear() : today.getYear();
                
                startDate = LocalDate.of(year, 1, 1);
                endDate = LocalDate.of(year, 12, 31);
                break;
                
            case "CUSTOM":
                startDate = requestDTO.getStartDate();
                endDate = requestDTO.getEndDate();
                break;
                
            default:
                throw new IllegalArgumentException("Invalid report type");
        }

        List<ExpenseReportDTO> result = new ArrayList<>();
        
        // Fetch expenses within date range
        List<Expenses> expenses = expensesRepository.findByDateBetween(startDate, endDate);
        
        // Convert expenses to DTOs
        for (Expenses expense : expenses) {
            // Calculate totalAmount if it's not set
            double calculatedTotal = expense.getTotalAmount();
            if (calculatedTotal == 0 && expense.getExpensesDetails() != null) {
                calculatedTotal = expense.getExpensesDetails().stream()
                    .mapToDouble(ExpensesDetails::getAmount)
                    .sum();
            }
    
            // Calculate closingBalance if it's not set
            double calculatedClosing = expense.getClosingBalance();
            if (calculatedClosing == 0) {
                calculatedClosing = expense.getOpeningBalance() + expense.getAmountWithdrawn() - calculatedTotal;
            }
    
            ExpenseReportDTO dto = new ExpenseReportDTO(
                expense.getId(),
                expense.getDate(),
                expense.getAccountNo() != null ? String.valueOf(expense.getAccountNo().getAccountNo()) : null,
                expense.getChequeNo(),
                expense.getOpeningBalance(),
                expense.getAmountWithdrawn(),
                calculatedTotal,
                calculatedClosing,
                new ArrayList<>()
            );
            
            // Add expense details
            if (expense.getExpensesDetails() != null) {
                for (ExpensesDetails detail : expense.getExpensesDetails()) {
                    dto.getDetails().add(new ExpenseDetailDTO(
                        detail.getDescription(),
                        detail.getAmount()
                    ));
                }
            }
            
            result.add(dto);
        }
        
        return result;
    }

    public ExpenseReportSummaryDTO getExpenseSummary(ExpenseReportRequestDTO requestDTO) {
        List<ExpenseReportDTO> expenses = getExpenseReport(requestDTO);
        
        // Calculate totals
        double totalWithdrawn = 0;
        double totalExpenses = 0;
        
        for (ExpenseReportDTO expense : expenses) {
            totalWithdrawn += expense.getAmountWithdrawn();
            totalExpenses += expense.getTotalAmount();
        }
        
        // Create summary
        return new ExpenseReportSummaryDTO(
            getStartDateFromReportType(requestDTO),
            getEndDateFromReportType(requestDTO),
            totalWithdrawn,
            totalExpenses,
            expenses.size()
        );
    }
    
    private LocalDate getStartDateFromReportType(ExpenseReportRequestDTO requestDTO) {
        LocalDate today = LocalDate.now();
        switch (requestDTO.getReportType().toUpperCase()) {
            case "MONTHLY":
                int month = requestDTO.getMonth() != null ? requestDTO.getMonth() : today.getMonthValue();
                int year = requestDTO.getYear() != null ? requestDTO.getYear() : today.getYear();
                return LocalDate.of(year, month, 1);
            case "YEARLY":
                year = requestDTO.getYear() != null ? requestDTO.getYear() : today.getYear();
                return LocalDate.of(year, 1, 1);
            default:
                return requestDTO.getStartDate();
        }
    }
    
    private LocalDate getEndDateFromReportType(ExpenseReportRequestDTO requestDTO) {
        LocalDate today = LocalDate.now();
        switch (requestDTO.getReportType().toUpperCase()) {
            case "MONTHLY":
                int month = requestDTO.getMonth() != null ? requestDTO.getMonth() : today.getMonthValue();
                int year = requestDTO.getYear() != null ? requestDTO.getYear() : today.getYear();
                return LocalDate.of(year, month, 1)
                              .withDayOfMonth(LocalDate.of(year, month, 1).lengthOfMonth());
            case "YEARLY":
                year = requestDTO.getYear() != null ? requestDTO.getYear() : today.getYear();
                return LocalDate.of(year, 12, 31);
            default:
                return requestDTO.getEndDate();
        }
    }
}