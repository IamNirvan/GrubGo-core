package com.iamnirvan.restaurant.core.controllers;

import com.iamnirvan.restaurant.core.models.requests.address.AddressCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.address.AddressUpdateRequest;
import com.iamnirvan.restaurant.core.services.IAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/customer/address")
public class AddressController {
    private final IAddressService addressService;

    @GetMapping
    public ResponseEntity<?> getCustomers(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "customerId", required = false) Long customerId) {
        return ResponseEntity.ok(addressService.getAddresses(id, customerId));
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(@Valid @RequestBody List<AddressCreateRequest> requests) {
        return ResponseEntity.ok(addressService.createAddress(requests));
    }

    @PutMapping
    public ResponseEntity<?> updateCustomer(@Valid @RequestBody List<AddressUpdateRequest> requests) {
        return ResponseEntity.ok(addressService.updateAddress(requests));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCustomer(@RequestParam(value = "ids") List<Long> ids) {
        return ResponseEntity.ok(addressService.deleteAddress(ids));
    }
}
