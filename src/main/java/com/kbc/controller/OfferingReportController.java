package com.kbc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kbc.service.OfferingReportService;
import com.kbc.service.OfferingReportService.OfferingReportDTO;
import com.kbc.service.OfferingReportService.OfferingReportRequestDTO;
import com.kbc.service.OfferingReportService.OfferingReportSummaryDTO;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/reports")
public class OfferingReportController {

    @Autowired
    private OfferingReportService offeringReportService;

    @PostMapping("/offerings")
    public ResponseEntity<List<OfferingReportDTO>> getOfferingReport(@RequestBody OfferingReportRequestDTO requestDTO) {
        List<OfferingReportDTO> report = offeringReportService.getOfferingReport(requestDTO);
        return ResponseEntity.ok(report);
    }

    @PostMapping("/offerings/summary")
    public ResponseEntity<OfferingReportSummaryDTO> getOfferingSummary(@RequestBody OfferingReportRequestDTO requestDTO) {
        OfferingReportSummaryDTO summary = offeringReportService.getOfferingSummary(requestDTO);
        return ResponseEntity.ok(summary);
    }
}