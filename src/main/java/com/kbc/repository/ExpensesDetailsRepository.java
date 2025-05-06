package com.kbc.repository;

import com.kbc.model.ExpensesDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExpensesDetailsRepository extends JpaRepository<ExpensesDetails, UUID> {
    // Add this to ExpensesDetailsRepository
    List<ExpensesDetails> findByExpensesId(UUID expenseId);
}
