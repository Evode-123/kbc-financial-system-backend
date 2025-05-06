package com.kbc.repository;

import com.kbc.model.BankOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface BankOfferingRepository extends JpaRepository<BankOffering, UUID> {
    @Query("SELECT COALESCE(SUM(b.amount), 0) FROM BankOffering b")
    float sumAllAmounts();
    
    @Query("SELECT COALESCE(SUM(b.bankTithe), 0) FROM BankOffering b")
    float sumAllTithes();

    @Query("SELECT SUM(b.amount) FROM BankOffering b WHERE b.date BETWEEN :start AND :end")
    Float sumAmountByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT SUM(b.bankTithe) FROM BankOffering b WHERE b.date BETWEEN :start AND :end")
    Float sumTitheByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    List<BankOffering> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
