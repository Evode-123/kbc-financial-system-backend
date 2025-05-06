package com.kbc.service;

import com.kbc.model.Sale;
import com.kbc.model.SaleItem;
import com.kbc.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final BooksService booksService;

    @Autowired
    public SaleService(SaleRepository saleRepository, BooksService booksService) {
        this.saleRepository = saleRepository;
        this.booksService = booksService;
    }

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    public Optional<Sale> getSaleById(Long id) {
        return saleRepository.findById(id);
    }

    @Transactional
    public Sale createSale(Sale sale) {
        // Calculate total amount
        float total = 0;
        for (SaleItem item : sale.getItems()) {
            total += item.getUnitPrice() * item.getQuantity();
            item.setSale(sale);
            
            // Update book stock
            if (!booksService.updateBookStock(item.getBook().getId(), item.getQuantity())) {
                throw new RuntimeException("Insufficient stock for book: " + item.getBook().getTitle());
            }
        }
        
        sale.setTotalAmount(total);
        sale.setSaleDate(LocalDateTime.now());
        return saleRepository.save(sale);
    }

    public void deleteSale(Long id) {
        saleRepository.deleteById(id);
    }

    public List<Sale> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.findBySaleDateBetween(startDate, endDate);
    }
}