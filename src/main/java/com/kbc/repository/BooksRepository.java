package com.kbc.repository;

import com.kbc.model.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BooksRepository extends JpaRepository<Books, UUID> {
    List<Books> findByTitleContainingIgnoreCase(String title);
    List<Books> findByCategoryId(UUID categoryId);
}