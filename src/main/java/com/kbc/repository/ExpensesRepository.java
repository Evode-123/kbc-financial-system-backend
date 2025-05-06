package com.kbc.repository;

import com.kbc.model.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, UUID> {
    // Updated to use totalAmount instead of totalExpenses
    @Query("SELECT SUM(e.totalAmount) FROM Expenses e WHERE e.date BETWEEN :start AND :end")
    Float sumTotalExpensesByDateRange(@Param("start") LocalDate start, @Param("end") LocalDate end);
    
    // This one is correct as amountWithdrawn exists in the model
    @Query("SELECT SUM(e.amountWithdrawn) FROM Expenses e WHERE e.date BETWEEN :start AND :end")
    Float sumAmountWithdrawnByDateRange(@Param("start") LocalDate start, @Param("end") LocalDate end);
    
    // Updated to use closingBalance instead of remainBalance
    @Query("SELECT SUM(e.closingBalance) FROM Expenses e WHERE e.date BETWEEN :start AND :end")
    Float sumClosingBalanceByDateRange(@Param("start") LocalDate start, @Param("end") LocalDate end);
    
    List<Expenses> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<Expenses> findByChequeNo(String chequeNo);
    Optional<Expenses> findTopByOrderByDateDesc();

    Optional<Expenses> findTopByOrderByDateDescCreatedAtDesc();

    List<Expenses> findByDateAfterOrderByDateAscCreatedAtAsc(LocalDate date);
    List<Expenses> findAllByOrderByDateAscCreatedAtAsc();
}