package com.iamnirvan.restaurant.core.controllers;

import com.iamnirvan.restaurant.core.models.requests.customer.CustomerRegisterRequest;
import com.iamnirvan.restaurant.core.models.requests.customer.CustomerUpdateRequest;
import com.iamnirvan.restaurant.core.services.ICustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/customer")
public class CustomerController {
    private final ICustomerService customerService;

    @GetMapping
    public ResponseEntity<?> getCustomers(@RequestParam(value = "id", required = false) Long id, Principal principal) {
        return ResponseEntity.ok(customerService.getCustomers(id));
    }

    @PostMapping("/register")
    public ResponseEntity<?> createCustomer(@Valid @RequestBody CustomerRegisterRequest request) {
        return ResponseEntity.ok(customerService.registerCustomer(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable("id") Long id, @Valid @RequestBody CustomerUpdateRequest request) {
        return ResponseEntity.ok(customerService.updateCustomer(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("id") Long id) {
        return ResponseEntity.ok(customerService.deleteCustomer(id));
    }
}
