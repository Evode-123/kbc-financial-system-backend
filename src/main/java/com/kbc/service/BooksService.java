package com.kbc.service;

import com.kbc.model.Books;
import com.kbc.repository.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Books> getAllBooks() {
        return booksRepository.findAll();
    }

    public Optional<Books> getBookById(UUID id) {
        return booksRepository.findById(id);
    }

    public Books saveBook(Books book) {
        return booksRepository.save(book);
    }

    public void deleteBook(UUID id) {
        booksRepository.deleteById(id);
    }

    public List<Books> searchBooksByTitle(String title) {
        return booksRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Books> getBooksByCategory(UUID categoryId) {
        return booksRepository.findByCategoryId(categoryId);
    }

    public boolean updateBookStock(UUID bookId, int quantity) {
        Optional<Books> bookOpt = booksRepository.findById(bookId);
        if (bookOpt.isPresent()) {
            Books book = bookOpt.get();
            int newQuantity = book.getStockQuantity() - quantity;
            if (newQuantity >= 0) {
                book.setStockQuantity(newQuantity);
                booksRepository.save(book);
                return true;
            }
        }
        return false;
    }
}