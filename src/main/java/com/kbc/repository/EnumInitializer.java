package com.kbc.repository;

import java.util.Arrays;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.kbc.model.BooksCategories;
import com.kbc.model.Currency;
import com.kbc.model.Services;

@Component
public class EnumInitializer implements ApplicationRunner {

    @Autowired
    private CurrencyRepository currencyRepository;
    
    @Autowired
    private org.springframework.data.jpa.repository.JpaRepository<Services, UUID> serviceRepository;

    @Autowired
    private BooksCategoriesRepository booksCategoriesRepository;

    @Override
    public void run(ApplicationArguments args) {
        // Initialize Currency enum values
        if (currencyRepository.count() == 0) {
            Arrays.stream(Currency.CurrencyName.values()).forEach(currencyName -> {
                Currency currency = new Currency();
                currency.setCurrencyName(currencyName);
                currencyRepository.save(currency);
                System.out.println("Added currency: " + currencyName);
            });
        }
        
        // Initialize Service enum values
        if (serviceRepository.count() == 0) {
            Arrays.stream(Services.ServiceName.values()).forEach(serviceName -> {
                Services service = new Services();
                service.setServiceName(serviceName);
                serviceRepository.save(service);
                System.out.println("Added service: " + serviceName);
            });
        }

        if(booksCategoriesRepository.count() == 0) {
            Arrays.stream(BooksCategories.Category.values()).forEach(category -> {
                BooksCategories booksCategories = new BooksCategories();
                booksCategories.setCategory(category);
                booksCategoriesRepository.save(booksCategories);
                System.out.println("Added category: " + category);
            });
        }
    }
}