package com.kbc.controller;

import com.kbc.model.MomoOffering;
import com.kbc.service.MomoOfferingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "https://kingdom-believers-church.vercel.app")
@RequestMapping("/momo-offerings")
public class MomoOfferingController {

    @Autowired
    private MomoOfferingService momoOfferingService;

    // Create a new Momo Offering
    @PostMapping("/add")
    public ResponseEntity<MomoOffering> addMomoOffering(@RequestBody MomoOffering momoOffering) {
        MomoOffering savedOffering = momoOfferingService.saveMomoOffering(momoOffering);
        return ResponseEntity.ok(savedOffering);
    }

    // Get all Momo Offerings
    @GetMapping("/get-all")
    public ResponseEntity<List<MomoOffering>> getAllMomoOfferings() {
        return ResponseEntity.ok(momoOfferingService.getAllMomoOfferings());
    }

    // Get a Momo Offering by ID
    @GetMapping("/{id}")
    public ResponseEntity<MomoOffering> getMomoOfferingById(@PathVariable UUID id) {
        return momoOfferingService.getMomoOfferingById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update a Momo Offering
    @PutMapping("/{id}")
    public ResponseEntity<MomoOffering> updateMomoOffering(@PathVariable UUID id, @RequestBody MomoOffering momoOffering) {
        return momoOfferingService.updateMomoOffering(id, momoOffering)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a Momo Offering
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMomoOffering(@PathVariable UUID id) {
        if (!momoOfferingService.getMomoOfferingById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        momoOfferingService.deleteMomoOffering(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/totals")
    public Map<String, Float> getTotals() {
        Map<String, Float> totals = new HashMap<>();
        totals.put("offerings", momoOfferingService.getTotalOfferings());
        totals.put("tithe", momoOfferingService.getTotalTithe());
        totals.put("net", momoOfferingService.getNetAmount());
        return totals;
    }

}
