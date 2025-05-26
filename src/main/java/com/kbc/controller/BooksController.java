package com.kbc.controller;

import com.kbc.model.Books;
import com.kbc.service.BooksService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "https://kingdom-believers-church.vercel.app")
@RequestMapping("/api/books")
public class BooksController {

    private final BooksService booksService;

    public BooksController(BooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping
    public ResponseEntity<List<Books>> getAllBooks() {
        return ResponseEntity.ok(booksService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Books> getBookById(@PathVariable UUID id) {
        return booksService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Books> createBook(@RequestBody Books book) {
        return ResponseEntity.status(HttpStatus.CREATED).body(booksService.saveBook(book));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Books> updateBook(@PathVariable UUID id, @RequestBody Books book) {
        return booksService.getBookById(id)
                .map(existingBook -> {
                    book.setId(id);
                    return ResponseEntity.ok(booksService.saveBook(book));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        if (booksService.getBookById(id).isPresent()) {
            booksService.deleteBook(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Books>> searchBooks(@RequestParam String title) {
        return ResponseEntity.ok(booksService.searchBooksByTitle(title));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Books>> getBooksByCategory(@PathVariable UUID categoryId) {
        return ResponseEntity.ok(booksService.getBooksByCategory(categoryId));
    }
}