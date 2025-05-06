package com.kbc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kbc.service.ExpenseReportService;
import com.kbc.service.ExpenseReportService.ExpenseReportDTO;
import com.kbc.service.ExpenseReportService.ExpenseReportRequestDTO;
import com.kbc.service.ExpenseReportService.ExpenseReportSummaryDTO;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/reports")
public class ExpenseReportController {

    @Autowired
    private ExpenseReportService expenseReportService;

    @PostMapping("/expenses")
    public ResponseEntity<List<ExpenseReportDTO>> getExpenseReport(@RequestBody ExpenseReportRequestDTO requestDTO) {
        List<ExpenseReportDTO> report = expenseReportService.getExpenseReport(requestDTO);
        return ResponseEntity.ok(report);
    }

    @PostMapping("/expenses/summary")
    public ResponseEntity<ExpenseReportSummaryDTO> getExpenseSummary(@RequestBody ExpenseReportRequestDTO requestDTO) {
        ExpenseReportSummaryDTO summary = expenseReportService.getExpenseSummary(requestDTO);
        return ResponseEntity.ok(summary);
    }
}