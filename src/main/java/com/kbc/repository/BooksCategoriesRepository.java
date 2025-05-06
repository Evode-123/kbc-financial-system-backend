package com.kbc.repository;

import com.kbc.model.BooksCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BooksCategoriesRepository extends JpaRepository<BooksCategories, UUID> {
    BooksCategories findByCategory(BooksCategories.Category category);
}