package com.kbc.repository;

import com.kbc.model.OfferingTypeMomo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OfferingTypeMomoRepository extends JpaRepository<OfferingTypeMomo, UUID> {
    // Fetch only the momoCode by OfferingTypeMomo ID
    @Query("SELECT o.momoCode FROM OfferingTypeMomo o WHERE o.id = :id")
    Optional<String> findMomoCodeByOfferingTypeMomoId(UUID id);
}
