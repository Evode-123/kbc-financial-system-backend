package com.kbc.controller;

import com.kbc.model.OfferingTypeMomo;
import com.kbc.service.OfferingTypeMomoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "https://kingdom-believers-church.vercel.app")
@RequestMapping("/offering-type-momos")
public class OfferingTypeMomoController {

    @Autowired
    private OfferingTypeMomoService offeringTypeMomoService;

    // Get all OfferingTypeMomos
    @GetMapping("/get-all-offeringtype-momo")
    public ResponseEntity<List<OfferingTypeMomo>> getAllOfferingTypeMomos() {
        List<OfferingTypeMomo> offeringTypeMomos = offeringTypeMomoService.getAllOfferingTypeMomos();
        return new ResponseEntity<>(offeringTypeMomos, HttpStatus.OK);
    }

    // Get OfferingTypeMomo by ID
    @GetMapping("/{id}")
    public ResponseEntity<OfferingTypeMomo> getOfferingTypeMomoById(@PathVariable UUID id) {
        Optional<OfferingTypeMomo> offeringTypeMomo = offeringTypeMomoService.getOfferingTypeMomoById(id);
        return offeringTypeMomo
                .map(momo -> new ResponseEntity<>(momo, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create a new OfferingTypeMomo
    @PostMapping("/add-offeringcategory-momo")
    public ResponseEntity<OfferingTypeMomo> createOfferingTypeMomo(@RequestBody OfferingTypeMomo offeringTypeMomo) {
        OfferingTypeMomo savedOfferingTypeMomo = offeringTypeMomoService.saveOfferingTypeMomo(offeringTypeMomo);
        return new ResponseEntity<>(savedOfferingTypeMomo, HttpStatus.CREATED);
    }

    // Update an existing OfferingTypeMomo
    @PutMapping("/{id}")
    public ResponseEntity<OfferingTypeMomo> updateOfferingTypeMomo(
            @PathVariable UUID id, @RequestBody OfferingTypeMomo offeringTypeMomoDetails) {
        try {
            OfferingTypeMomo updatedOfferingTypeMomo = offeringTypeMomoService.updateOfferingTypeMomo(id, offeringTypeMomoDetails);
            return new ResponseEntity<>(updatedOfferingTypeMomo, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete an OfferingTypeMomo by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOfferingTypeMomo(@PathVariable UUID id) {
        offeringTypeMomoService.deleteOfferingTypeMomo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/momoCode")
    public Optional<String> getMomoCodeByOfferingTypeMomoId(@PathVariable UUID id) {
        return offeringTypeMomoService.getMomoCodeByOfferingTypeMomoId(id);
    }

}
