package com.kbc.service;

import com.kbc.model.BooksCategories;
import com.kbc.repository.BooksCategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BooksCategoriesService {

    private final BooksCategoriesRepository categoriesRepository;

    @Autowired
    public BooksCategoriesService(BooksCategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public List<BooksCategories> getAllCategories() {
        return categoriesRepository.findAll();
    }

    public Optional<BooksCategories> getCategoryById(UUID id) {
        return categoriesRepository.findById(id);
    }

    public BooksCategories saveCategory(BooksCategories category) {
        return categoriesRepository.save(category);
    }

    public void deleteCategory(UUID id) {
        categoriesRepository.deleteById(id);
    }

    public BooksCategories findByCategory(BooksCategories.Category category) {
        return categoriesRepository.findByCategory(category);
    }
}