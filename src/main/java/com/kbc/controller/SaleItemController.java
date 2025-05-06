package com.kbc.controller;

import com.kbc.model.SaleItem;
import com.kbc.service.SaleItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/sale-items")
public class SaleItemController {

    private final SaleItemService saleItemService;

    @Autowired
    public SaleItemController(SaleItemService saleItemService) {
        this.saleItemService = saleItemService;
    }

    @GetMapping
    public ResponseEntity<List<SaleItem>> getAllSaleItems() {
        return ResponseEntity.ok(saleItemService.getAllSaleItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleItem> getSaleItemById(@PathVariable Long id) {
        return saleItemService.getSaleItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SaleItem> createSaleItem(@RequestBody SaleItem saleItem) {
        return ResponseEntity.status(HttpStatus.CREATED).body(saleItemService.saveSaleItem(saleItem));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleItem> updateSaleItem(@PathVariable Long id, @RequestBody SaleItem saleItem) {
        return saleItemService.getSaleItemById(id)
                .map(existingSaleItem -> {
                    saleItem.setId(id);
                    return ResponseEntity.ok(saleItemService.saveSaleItem(saleItem));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSaleItem(@PathVariable Long id) {
        if (saleItemService.getSaleItemById(id).isPresent()) {
            saleItemService.deleteSaleItem(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/sale/{saleId}")
    public ResponseEntity<List<SaleItem>> getSaleItemsBySaleId(@PathVariable Long saleId) {
        return ResponseEntity.ok(saleItemService.getSaleItemsBySaleId(saleId));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<SaleItem>> getSaleItemsByBookId(@PathVariable UUID bookId) {
        return ResponseEntity.ok(saleItemService.getSaleItemsByBookId(bookId));
    }
}