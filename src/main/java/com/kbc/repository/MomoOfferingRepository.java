package com.kbc.repository;

import com.kbc.model.MomoOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MomoOfferingRepository extends JpaRepository<MomoOffering, UUID> {
    @Query("SELECT COALESCE(SUM(m.amount), 0) FROM MomoOffering m")
    float sumAllAmounts();
    
    @Query("SELECT COALESCE(SUM(m.momoTithe), 0) FROM MomoOffering m")
    float sumAllTithes();

     @Query("SELECT SUM(m.amount) FROM MomoOffering m WHERE m.date BETWEEN :start AND :end")
    Float sumAmountByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT SUM(m.momoTithe) FROM MomoOffering m WHERE m.date BETWEEN :start AND :end")
    Float sumTitheByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    List<MomoOffering> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
