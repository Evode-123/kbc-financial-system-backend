package com.kbc.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kbc.model.BankOffering;
import com.kbc.model.CashOffering;
import com.kbc.model.MomoOffering;
import com.kbc.repository.BankOfferingRepository;
import com.kbc.repository.CashOfferingRepository;
import com.kbc.repository.MomoOfferingRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
public class OfferingReportService {

    @Autowired
    private BankOfferingRepository bankOfferingRepository;
    
    @Autowired
    private CashOfferingRepository cashOfferingRepository;
    
    @Autowired
    private MomoOfferingRepository momoOfferingRepository;

    // DTOs defined within the service class
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OfferingReportDTO {
        private UUID id;
        private LocalDateTime date;
        private String offeringType;
        private String sourceType; // BANK, CASH, MOMO
        private String accountOrMomoCode;
        private String currencyName;
        private float amount;
        private float tithe;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OfferingReportSummaryDTO {
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private float totalAmount;
        private float totalTithe;
        private int transactionCount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OfferingReportRequestDTO {
        private String reportType; // WEEKLY, MONTHLY, YEARLY, CUSTOM
        private LocalDateTime startDate; // For CUSTOM type
        private LocalDateTime endDate; // For CUSTOM type
        private Integer month; // For monthly reports (1-12)
        private Integer year;  // For yearly and monthly reports
    }

    public List<OfferingReportDTO> getOfferingReport(OfferingReportRequestDTO requestDTO) {
        LocalDateTime startDate, endDate;
        LocalDate today = LocalDate.now();
        
        switch (requestDTO.getReportType().toUpperCase()) {
            case "MONTHLY":
                // Handle selected month (default to current month if not specified)
                int month = requestDTO.getMonth() != null ? requestDTO.getMonth() : today.getMonthValue();
                int year = requestDTO.getYear() != null ? requestDTO.getYear() : today.getYear();
                
                LocalDate monthDate = LocalDate.of(year, month, 1);
                startDate = monthDate.atStartOfDay();
                endDate = monthDate.withDayOfMonth(monthDate.lengthOfMonth())
                                  .atTime(LocalTime.MAX);
                break;
                
            case "YEARLY":
                // Handle selected year (default to current year if not specified)
                year = requestDTO.getYear() != null ? requestDTO.getYear() : today.getYear();
                
                startDate = LocalDate.of(year, 1, 1).atStartOfDay();
                endDate = LocalDate.of(year, 12, 31).atTime(LocalTime.MAX);
                break;
                
            case "CUSTOM":
                startDate = requestDTO.getStartDate();
                endDate = requestDTO.getEndDate();
                break;
                
            default:
                throw new IllegalArgumentException("Invalid report type");
        }

        List<OfferingReportDTO> result = new ArrayList<>();
        
        // Fetch data from all three offering types using existing repositories
        List<BankOffering> bankOfferings = bankOfferingRepository.findByDateBetween(startDate, endDate);
        List<CashOffering> cashOfferings = cashOfferingRepository.findByDateBetween(startDate, endDate);
        List<MomoOffering> momoOfferings = momoOfferingRepository.findByDateBetween(startDate, endDate);
        
        // Convert bank offerings to DTOs
        for (BankOffering bo : bankOfferings) {
            OfferingReportDTO dto = new OfferingReportDTO(
                bo.getId(),
                bo.getDate(),
                bo.getOfferingType().getOfferingTypeName(),
                "BANK",
                String.valueOf(bo.getAccountNo().getAccountNo()),
                bo.getAccountNo().getCurrency().getCurrencyName().toString(),
                bo.getAmount(),
                bo.getBankTithe() != null ? bo.getBankTithe() : 0f
            );
            result.add(dto);
        }
        
        // Convert cash offerings to DTOs
        for (CashOffering co : cashOfferings) {
            OfferingReportDTO dto = new OfferingReportDTO(
                co.getId(),
                co.getDate(),
                co.getOfferingType().getOfferingTypeName(),
                "CASH",
                String.valueOf(co.getAccountNo().getAccountNo()),
                co.getCurrency().getCurrencyName().toString(),
                co.getTotalAmount(),
                co.getCashTithe() != null ? co.getCashTithe() : 0f
            );
            result.add(dto);
        }
        
        // Convert momo offerings to DTOs
        for (MomoOffering mo : momoOfferings) {
            OfferingReportDTO dto = new OfferingReportDTO(
                mo.getId(),
                mo.getDate(),
                mo.getOfferingType().getOfferingTypeName(),
                "MOMO",
                String.valueOf(mo.getMomoCode().getMomoCode()),
                "FRW", // Assuming Mobile Money is always in FRW
                mo.getAmount(),
                mo.getMomoTithe() != null ? mo.getMomoTithe() : 0f
            );
            result.add(dto);
        }
        
        return result;
    }

    public OfferingReportSummaryDTO getOfferingSummary(OfferingReportRequestDTO requestDTO) {
        List<OfferingReportDTO> offerings = getOfferingReport(requestDTO);
        
        // Calculate totals
        float totalAmount = 0;
        float totalTithe = 0;
        
        for (OfferingReportDTO offering : offerings) {
            totalAmount += offering.getAmount();
            totalTithe += offering.getTithe();
        }
        
        // Create summary
        return new OfferingReportSummaryDTO(
            getStartDateFromReportType(requestDTO),
            getEndDateFromReportType(requestDTO),
            totalAmount,
            totalTithe,
            offerings.size()
        );
    }
    
    private LocalDateTime getStartDateFromReportType(OfferingReportRequestDTO requestDTO) {
        LocalDate today = LocalDate.now();
        switch (requestDTO.getReportType().toUpperCase()) {
            case "MONTHLY":
                int month = requestDTO.getMonth() != null ? requestDTO.getMonth() : today.getMonthValue();
                int year = requestDTO.getYear() != null ? requestDTO.getYear() : today.getYear();
                return LocalDate.of(year, month, 1).atStartOfDay();
            case "YEARLY":
                year = requestDTO.getYear() != null ? requestDTO.getYear() : today.getYear();
                return LocalDate.of(year, 1, 1).atStartOfDay();
            default:
                return requestDTO.getStartDate();
        }
    }
    
    private LocalDateTime getEndDateFromReportType(OfferingReportRequestDTO requestDTO) {
        LocalDate today = LocalDate.now();
        switch (requestDTO.getReportType().toUpperCase()) {
            case "MONTHLY":
                int month = requestDTO.getMonth() != null ? requestDTO.getMonth() : today.getMonthValue();
                int year = requestDTO.getYear() != null ? requestDTO.getYear() : today.getYear();
                return LocalDate.of(year, month, 1)
                              .withDayOfMonth(LocalDate.of(year, month, 1).lengthOfMonth())
                              .atTime(LocalTime.MAX);
            case "YEARLY":
                year = requestDTO.getYear() != null ? requestDTO.getYear() : today.getYear();
                return LocalDate.of(year, 12, 31).atTime(LocalTime.MAX);
            default:
                return requestDTO.getEndDate();
        }
    }
}