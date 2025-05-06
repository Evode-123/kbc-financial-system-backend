package com.kbc.controller;

import com.kbc.model.BooksCategories;
import com.kbc.service.BooksCategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/categories")
public class BooksCategoriesController {

    private final BooksCategoriesService categoriesService;

    @Autowired
    public BooksCategoriesController(BooksCategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping
    public ResponseEntity<List<BooksCategories>> getAllCategories() {
        return ResponseEntity.ok(categoriesService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BooksCategories> getCategoryById(@PathVariable UUID id) {
        return categoriesService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BooksCategories> createCategory(@RequestBody BooksCategories category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriesService.saveCategory(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BooksCategories> updateCategory(@PathVariable UUID id, @RequestBody BooksCategories category) {
        return categoriesService.getCategoryById(id)
                .map(existingCategory -> {
                    category.setId(id);
                    return ResponseEntity.ok(categoriesService.saveCategory(category));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        if (categoriesService.getCategoryById(id).isPresent()) {
            categoriesService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}