package com.kbc.controller;

import com.kbc.model.Currency;
import com.kbc.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currency")
@CrossOrigin(origins = "http://localhost:3000")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/get-all")
    public ResponseEntity<List<Currency>> getAllCurrencies() {
        List<Currency> currencies = currencyService.getAllCurrencies();
        return ResponseEntity.ok(currencies);
    }
}
