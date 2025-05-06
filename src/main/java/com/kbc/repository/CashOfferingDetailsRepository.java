package com.kbc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kbc.model.CashOfferingDetails;

import java.util.List;
import java.util.UUID;

public interface CashOfferingDetailsRepository extends JpaRepository<CashOfferingDetails, UUID> {
    List<CashOfferingDetails> findByCashOfferingId(UUID cashOfferingId);
}
