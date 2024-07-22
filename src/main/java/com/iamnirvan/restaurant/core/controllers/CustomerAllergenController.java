package com.iamnirvan.restaurant.core.controllers;

import com.iamnirvan.restaurant.core.models.requests.customer_allergen.CustomerAllergenCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.customer_allergen.CustomerAllergenUpdateRequest;
import com.iamnirvan.restaurant.core.services.ICustomerAllergenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/customer/allergen")
public class CustomerAllergenController {
    private final ICustomerAllergenService allergenService;

    @GetMapping
    public ResponseEntity<?> getCustomers(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "customerId", required = false) Long customerId) {
        return ResponseEntity.ok(allergenService.getAllergens(id, customerId));
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(@Valid @RequestBody List<CustomerAllergenCreateRequest> requests) {
        return ResponseEntity.ok(allergenService.createAllergen(requests));
    }

    @PutMapping
    public ResponseEntity<?> updateCustomer(@Valid @RequestBody List<CustomerAllergenUpdateRequest> requests) {
        return ResponseEntity.ok(allergenService.updateAllergen(requests));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCustomer(@RequestParam(value = "ids") List<Long> ids) {
        return ResponseEntity.ok(allergenService.deleteAllergen(ids));
    }
}
