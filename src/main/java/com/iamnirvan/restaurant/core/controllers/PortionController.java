package com.iamnirvan.restaurant.core.controllers;

import com.iamnirvan.restaurant.core.models.requests.portion.PortionCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.portion.PortionUpdateRequest;
import com.iamnirvan.restaurant.core.services.IPortionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/portion")
public class PortionController {
    private final IPortionService portionService;

    @GetMapping
    public ResponseEntity<?> getPortions(@RequestParam(value = "id", required = false) Long id) {
        return new ResponseEntity<>(portionService.getPortions(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createPortion(@Valid @RequestBody List<PortionCreateRequest> requests) {
        return new ResponseEntity<>(portionService.createPortion(requests), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateCustomer(@Valid @RequestBody List<PortionUpdateRequest> requests) {
        return new ResponseEntity<>(portionService.updatePortion(requests), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCustomer(@RequestParam(value = "ids") List<Long> ids) {
        return new ResponseEntity<>(portionService.deletePortion(ids), HttpStatus.OK);
    }
}
