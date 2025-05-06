package com.kbc.repository;

import com.kbc.model.MomoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MomoCodeRepository extends JpaRepository<MomoCode, UUID> {
    List<MomoCode> findByStatus(String status); // Find accounts by status
    @Query("SELECT COUNT(m) FROM MomoCode m WHERE m.status = 'ACTIVE'")
    long countActiveMomoCodes();
}
