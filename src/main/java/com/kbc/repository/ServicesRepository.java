package com.kbc.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kbc.model.Services;

@Repository
public interface ServicesRepository extends JpaRepository<Services, UUID> {
}