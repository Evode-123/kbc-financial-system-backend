package com.kbc.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kbc.model.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, UUID> {
}