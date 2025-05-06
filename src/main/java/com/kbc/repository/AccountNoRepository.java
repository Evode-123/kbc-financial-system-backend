package com.kbc.repository;

import com.kbc.model.AccountNo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
 
@Repository
public interface AccountNoRepository extends JpaRepository<AccountNo, UUID> {
    List<AccountNo> findByStatus(String status); // Find accounts by status
    @Query("SELECT COUNT(a) FROM AccountNo a WHERE a.status = 'ACTIVE'")
    long countActiveAccounts();
}
