package com.kbc.repository;

import com.kbc.model.OfferingTypeAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OfferingTypeAccountRepository extends JpaRepository<OfferingTypeAccount, UUID> {
}
