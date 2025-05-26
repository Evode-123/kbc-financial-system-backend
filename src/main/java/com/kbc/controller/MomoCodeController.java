package com.kbc.controller;

import com.kbc.model.MomoCode;
import com.kbc.service.MomoCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "https://kingdom-believers-church.vercel.app")
@RequestMapping("/momocodes")
public class MomoCodeController {

    @Autowired
    private MomoCodeService momoCodeService;

    // Create a new MomoCode
    @PostMapping("/add-momocode")
    public ResponseEntity<MomoCode> createMomoCode(@RequestBody MomoCode momoCode) {
        MomoCode savedMomoCode = momoCodeService.saveNewMomoCode(momoCode);
        return new ResponseEntity<>(savedMomoCode, HttpStatus.CREATED);
    }

    // Update an existing MomoCode
    @PutMapping("/{id}")
    public ResponseEntity<MomoCode> updateMomoCode(@PathVariable UUID id, @RequestBody MomoCode momoCode) {
        MomoCode updatedMomoCode = momoCodeService.updateMomoCode(id, momoCode);
        return new ResponseEntity<>(updatedMomoCode, HttpStatus.OK);
    }

    // Get all MomoCodes
    @GetMapping("/get-all-momocodes")
    public ResponseEntity<List<MomoCode>> getAllMomoCodes() {
        List<MomoCode> momoCodes = momoCodeService.getAllMomoCodes();
        return new ResponseEntity<>(momoCodes, HttpStatus.OK);
    }

    // Get a MomoCode by ID
    @GetMapping("/{id}")
    public ResponseEntity<MomoCode> getMomoCodeById(@PathVariable UUID id) {
        Optional<MomoCode> momoCode = momoCodeService.getMomoCodeById(id);
        return momoCode.map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete a MomoCode by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMomoCode(@PathVariable UUID id) {
        momoCodeService.deleteMomoCode(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Deactivate an AccountNo
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<MomoCode> deactivateMomoCode(@PathVariable UUID id) {
        MomoCode deactivateMomoCode = momoCodeService.deactivateMomoCode(id);
        return new ResponseEntity<>(deactivateMomoCode, HttpStatus.OK);
    }

    // Reactivate an AccountNo
    @PutMapping("/{id}/reactivate")
    public ResponseEntity<MomoCode> reactivateMomoCode(@PathVariable UUID id) {
        MomoCode reactivatedMomoCode = momoCodeService.reactivateMomoCode(id);
        return new ResponseEntity<>(reactivatedMomoCode, HttpStatus.OK);
    }

    @GetMapping("/active")
    public List<MomoCode> getActiveAccounts() {
        return momoCodeService.getActiveMomoCodes();
    }

}
