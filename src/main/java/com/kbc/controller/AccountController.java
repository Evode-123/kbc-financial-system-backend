package com.kbc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kbc.service.AccountService;
import com.kbc.service.AccountService.AccountStatsDTO;

import java.util.Map;

@RestController
@CrossOrigin(origins = "https://kingdom-believers-church.vercel.app")
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;
    
    /**
     * Get account statistics in DTO format
     * @return ResponseEntity containing account statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<AccountStatsDTO> getAccountStats() {
        AccountStatsDTO stats = accountService.getAccountStats();
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Get account statistics in Map format for flexible response structure
     * @return ResponseEntity containing account statistics as a Map
     */
    @GetMapping("/stats/map")
    public ResponseEntity<Map<String, Object>> getAccountStatsMap() {
        Map<String, Object> stats = accountService.getAccountStatsMap();
        return ResponseEntity.ok(stats);
    }
}