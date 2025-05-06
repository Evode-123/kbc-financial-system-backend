package com.kbc.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kbc.service.DashboardService;
import com.kbc.service.DashboardService.DashboardOverviewDTO;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/overview/{year}")
    public ResponseEntity<DashboardOverviewDTO> getDashboardOverview(@PathVariable int year) {
        DashboardOverviewDTO overview = dashboardService.getDashboardOverview(year);
        return ResponseEntity.ok(overview);
    }
    
    @GetMapping("/quick-stats")
    public ResponseEntity<Map<String, Object>> getQuickStats() {
        Map<String, Object> stats = dashboardService.getQuickStats();
        return ResponseEntity.ok(stats);
    }
}