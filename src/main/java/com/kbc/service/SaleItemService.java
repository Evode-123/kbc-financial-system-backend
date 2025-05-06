package com.kbc.service;

import com.kbc.model.SaleItem;
import com.kbc.repository.SaleItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SaleItemService {

    private final SaleItemRepository saleItemRepository;

    @Autowired
    public SaleItemService(SaleItemRepository saleItemRepository) {
        this.saleItemRepository = saleItemRepository;
    }

    public List<SaleItem> getAllSaleItems() {
        return saleItemRepository.findAll();
    }

    public Optional<SaleItem> getSaleItemById(Long id) {
        return saleItemRepository.findById(id);
    }

    public SaleItem saveSaleItem(SaleItem saleItem) {
        return saleItemRepository.save(saleItem);
    }

    public void deleteSaleItem(Long id) {
        saleItemRepository.deleteById(id);
    }

    public List<SaleItem> getSaleItemsBySaleId(Long saleId) {
        return saleItemRepository.findBySaleId(saleId);
    }

    public List<SaleItem> getSaleItemsByBookId(UUID bookId) {
        return saleItemRepository.findByBookId(bookId);
    }
}