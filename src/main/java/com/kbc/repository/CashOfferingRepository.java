package com.kbc.repository;

import com.kbc.model.CashOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CashOfferingRepository extends JpaRepository<CashOffering, UUID> {
    @Query("SELECT COALESCE(SUM(c.totalAmount), 0) FROM CashOffering c")
    float sumAllAmounts();
    
    @Query("SELECT COALESCE(SUM(c.cashTithe), 0) FROM CashOffering c")
    float sumAllTithes();

    @Query("SELECT SUM(c.totalAmount) FROM CashOffering c WHERE c.date BETWEEN :start AND :end")
    Float sumAmountByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT SUM(c.cashTithe) FROM CashOffering c WHERE c.date BETWEEN :start AND :end")
    Float sumTitheByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    List<CashOffering> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}