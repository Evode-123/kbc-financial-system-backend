package com.kbc.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kbc.service.OfferingReportService.OfferingReportRequestDTO;
import com.kbc.service.OfferingReportService.OfferingReportSummaryDTO;
import com.kbc.service.ExpenseReportService.ExpenseReportRequestDTO;
import com.kbc.service.ExpenseReportService.ExpenseReportSummaryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
public class DashboardService {

    @Autowired
    private OfferingReportService offeringReportService;
    
    @Autowired
    private ExpenseReportService expenseReportService;

    @Autowired
    private AccountService accountService;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardOverviewDTO {
        private OverallSummaryDTO overall;
        private List<MonthlyTrendDTO> monthlyTrends;
        private OfferingBreakdownDTO offeringBreakdown;
        private ExpenseBreakdownDTO expenseBreakdown;
    }
    
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OverallSummaryDTO {
        private double totalOfferings;
        private double totalTithes;
        private double totalExpenses;
        private double netBalance;
        private int offeringCount;
        private int expenseCount;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyTrendDTO {
        private String month;
        private double totalOfferings;
        private double totalExpenses;
        private double netBalance;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OfferingBreakdownDTO {
        private Map<String, Double> byOfferingType;
        private Map<String, Double> bySourceType;
        private Map<String, Double> byCurrency;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpenseBreakdownDTO {
        private double totalWithdrawn;
        private double totalExpensed;
        private double totalRemaining;
        private Map<String, Double> byCurrency;
    }
    
    public DashboardOverviewDTO getDashboardOverview(int year) {
        // Create a yearly request DTO for both services
        OfferingReportRequestDTO offeringRequest = new OfferingReportRequestDTO();
        offeringRequest.setReportType("YEARLY");
        offeringRequest.setYear(year);
        
        ExpenseReportRequestDTO expenseRequest = new ExpenseReportRequestDTO();
        expenseRequest.setReportType("YEARLY");
        expenseRequest.setYear(year);
        
        // Get yearly summaries
        OfferingReportSummaryDTO offeringSummary = offeringReportService.getOfferingSummary(offeringRequest);
        ExpenseReportSummaryDTO expenseSummary = expenseReportService.getExpenseSummary(expenseRequest);
        
        // Get detailed reports for breakdowns
        List<OfferingReportService.OfferingReportDTO> offerings = offeringReportService.getOfferingReport(offeringRequest);
        List<ExpenseReportService.ExpenseReportDTO> expenses = expenseReportService.getExpenseReport(expenseRequest);
        
        // Create overall summary
        OverallSummaryDTO overall = new OverallSummaryDTO(
            offeringSummary.getTotalAmount(),
            offeringSummary.getTotalTithe(),
            expenseSummary.getTotalExpenses(),
            offeringSummary.getTotalAmount() - expenseSummary.getTotalExpenses(),
            offeringSummary.getTransactionCount(),
            expenseSummary.getTransactionCount()
        );
        
        // Create monthly trends
        List<MonthlyTrendDTO> monthlyTrends = getMonthlyTrends(year);
        
        // Create offering breakdown
        OfferingBreakdownDTO offeringBreakdown = createOfferingBreakdown(offerings);
        
        // Create expense breakdown
        ExpenseBreakdownDTO expenseBreakdown = createExpenseBreakdown(expenses);
        
        return new DashboardOverviewDTO(overall, monthlyTrends, offeringBreakdown, expenseBreakdown);
    }
    
    private List<MonthlyTrendDTO> getMonthlyTrends(int year) {
        List<MonthlyTrendDTO> trends = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM");
        
        // Get data for each month
        for (int month = 1; month <= 12; month++) {
            OfferingReportRequestDTO offeringRequest = new OfferingReportRequestDTO();
            offeringRequest.setReportType("MONTHLY");
            offeringRequest.setMonth(month);
            offeringRequest.setYear(year);
            
            ExpenseReportRequestDTO expenseRequest = new ExpenseReportRequestDTO();
            expenseRequest.setReportType("MONTHLY");
            expenseRequest.setMonth(month);
            expenseRequest.setYear(year);
            
            OfferingReportSummaryDTO offeringSummary = offeringReportService.getOfferingSummary(offeringRequest);
            ExpenseReportSummaryDTO expenseSummary = expenseReportService.getExpenseSummary(expenseRequest);
            
            String monthName = YearMonth.of(year, month).format(formatter);
            double offerings = offeringSummary.getTotalAmount();
            double expenses = expenseSummary.getTotalExpenses();
            
            trends.add(new MonthlyTrendDTO(
                monthName,
                offerings,
                expenses,
                offerings - expenses
            ));
        }
        
        return trends;
    }
    
    private OfferingBreakdownDTO createOfferingBreakdown(List<OfferingReportService.OfferingReportDTO> offerings) {
        Map<String, Double> byOfferingType = new LinkedHashMap<>();
        Map<String, Double> bySourceType = new LinkedHashMap<>();
        Map<String, Double> byCurrency = new LinkedHashMap<>();
        
        // Aggregate data
        for (OfferingReportService.OfferingReportDTO offering : offerings) {
            // By offering type
            byOfferingType.put(
                offering.getOfferingType(),
                byOfferingType.getOrDefault(offering.getOfferingType(), 0.0) + offering.getAmount()
            );
            
            // By source type
            bySourceType.put(
                offering.getSourceType(),
                bySourceType.getOrDefault(offering.getSourceType(), 0.0) + offering.getAmount()
            );
            
            // By currency
            byCurrency.put(
                offering.getCurrencyName(),
                byCurrency.getOrDefault(offering.getCurrencyName(), 0.0) + offering.getAmount()
            );
        }
        
        return new OfferingBreakdownDTO(byOfferingType, bySourceType, byCurrency);
    }
    
    private ExpenseBreakdownDTO createExpenseBreakdown(List<ExpenseReportService.ExpenseReportDTO> expenses) {
        double totalWithdrawn = 0;
        double totalExpensed = 0;
        double totalRemaining = 0;
        Map<String, Double> byCurrency = new LinkedHashMap<>();
        
        // Aggregate data
        for (ExpenseReportService.ExpenseReportDTO expense : expenses) {
            totalWithdrawn += expense.getAmountWithdrawn();
            totalExpensed += expense.getTotalAmount(); // Changed from getTotalExpenses
            
            // Calculate remaining as closing balance
            totalRemaining += expense.getClosingBalance(); // Changed from getRemainBalance
            
            // By currency - we need to extract currency from accountNo or use a default
            // Since the updated model doesn't have currency, we'll use accountNo instead
            String currency = expense.getAccountNo() != null ? expense.getAccountNo() : "Unknown";
            byCurrency.put(
                currency,
                byCurrency.getOrDefault(currency, 0.0) + expense.getTotalAmount() // Changed from getTotalExpenses
            );
        }
        
        return new ExpenseBreakdownDTO(totalWithdrawn, totalExpensed, totalRemaining, byCurrency);
    }
    
    public Map<String, Object> getQuickStats() {
        Map<String, Object> stats = new LinkedHashMap<>();
        
        // Get current year data
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        int currentMonth = today.getMonthValue();
        
        // Current year totals
        OfferingReportRequestDTO yearOfferingRequest = new OfferingReportRequestDTO();
        yearOfferingRequest.setReportType("YEARLY");
        yearOfferingRequest.setYear(currentYear);
        
        ExpenseReportRequestDTO yearExpenseRequest = new ExpenseReportRequestDTO();
        yearExpenseRequest.setReportType("YEARLY");
        yearExpenseRequest.setYear(currentYear);
        
        OfferingReportSummaryDTO yearOfferingSummary = offeringReportService.getOfferingSummary(yearOfferingRequest);
        ExpenseReportSummaryDTO yearExpenseSummary = expenseReportService.getExpenseSummary(yearExpenseRequest);
        
        // Current month totals
        OfferingReportRequestDTO monthOfferingRequest = new OfferingReportRequestDTO();
        monthOfferingRequest.setReportType("MONTHLY");
        monthOfferingRequest.setMonth(currentMonth);
        monthOfferingRequest.setYear(currentYear);
        
        ExpenseReportRequestDTO monthExpenseRequest = new ExpenseReportRequestDTO();
        monthExpenseRequest.setReportType("MONTHLY");
        monthExpenseRequest.setMonth(currentMonth);
        monthExpenseRequest.setYear(currentYear);
        
        OfferingReportSummaryDTO monthOfferingSummary = offeringReportService.getOfferingSummary(monthOfferingRequest);
        ExpenseReportSummaryDTO monthExpenseSummary = expenseReportService.getExpenseSummary(monthExpenseRequest);
        
        // Add stats
        stats.put("currentYear", currentYear);
        stats.put("currentMonth", YearMonth.of(currentYear, currentMonth).format(DateTimeFormatter.ofPattern("MMMM")));
        
        // Yearly stats
        stats.put("yearlyOfferings", yearOfferingSummary.getTotalAmount());
        stats.put("yearlyTithes", yearOfferingSummary.getTotalTithe());
        stats.put("yearlyExpenses", yearExpenseSummary.getTotalExpenses());
        stats.put("yearlyNetBalance", yearOfferingSummary.getTotalAmount() - yearExpenseSummary.getTotalExpenses());
        
        // Monthly stats
        stats.put("monthlyOfferings", monthOfferingSummary.getTotalAmount());
        stats.put("monthlyTithes", monthOfferingSummary.getTotalTithe());
        stats.put("monthlyExpenses", monthExpenseSummary.getTotalExpenses());
        stats.put("monthlyNetBalance", monthOfferingSummary.getTotalAmount() - monthExpenseSummary.getTotalExpenses());
        
        // Add account statistics
        accountService.addAccountStatsToDashboard(stats);
        
        return stats;
    }
}