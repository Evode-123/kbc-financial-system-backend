package com.kbc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kbc.repository.AccountNoRepository;
import com.kbc.repository.MomoCodeRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AccountService {

    @Autowired
    private AccountNoRepository accountNoRepository;
    
    @Autowired
    private MomoCodeRepository momoCodeRepository;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountStatsDTO {
        private long totalBankAccounts;
        private long totalMomoAccounts;
        private long totalAccounts;
    }
    
    /**
     * Count all accounts in the system
     * @return AccountStatsDTO containing counts of different account types
     */
    public AccountStatsDTO getAccountStats() {
        long bankAccounts = accountNoRepository.countActiveAccounts();
        long momoAccounts = momoCodeRepository.countActiveMomoCodes();
        
        return new AccountStatsDTO(
            bankAccounts,
            momoAccounts,
            bankAccounts + momoAccounts
        );
    }
    
    /**
     * Get a map of account statistics for dashboard display
     * @return Map containing account statistics
     */
    public Map<String, Object> getAccountStatsMap() {
        Map<String, Object> stats = new LinkedHashMap<>();
        
        long bankAccounts = accountNoRepository.countActiveAccounts();
        long momoAccounts = momoCodeRepository.countActiveMomoCodes();
        long totalAccounts = bankAccounts + momoAccounts;
        
        stats.put("bankAccounts", bankAccounts);
        stats.put("momoAccounts", momoAccounts);
        stats.put("totalAccounts", totalAccounts);
        
        return stats;
    }
    
    /**
     * Add this method to the DashboardService
     * Updates the dashboard quick stats with account information
     */
    public void addAccountStatsToDashboard(Map<String, Object> dashboardStats) {
        long bankAccounts = accountNoRepository.countActiveAccounts();
        long momoAccounts = momoCodeRepository.countActiveMomoCodes();
        long totalAccounts = bankAccounts + momoAccounts;
        
        dashboardStats.put("bankAccounts", bankAccounts);
        dashboardStats.put("momoAccounts", momoAccounts);
        dashboardStats.put("totalAccounts", totalAccounts);
    }
}