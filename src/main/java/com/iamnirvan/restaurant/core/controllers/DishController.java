package com.iamnirvan.restaurant.core.controllers;

import com.iamnirvan.restaurant.core.models.requests.dish.DishCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.dish.DishUpdateRequest;
import com.iamnirvan.restaurant.core.services.IDishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/dish")
public class DishController {
    private final IDishService dishService;

    @GetMapping
    public ResponseEntity<?> getDishes(@RequestParam(value = "id", required = false) Long id) {
        return new ResponseEntity<>(dishService.getDishes(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createDish(@Valid @RequestBody List<DishCreateRequest> requests) {
        return new ResponseEntity<>(dishService.createDish(requests), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateCustomer(@Valid @RequestBody List<DishUpdateRequest> requests) {
        return new ResponseEntity<>(dishService.updateDish(requests), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCustomer(@RequestParam(value = "ids") List<Long> ids) {
        return new ResponseEntity<>(dishService.deleteDish(ids), HttpStatus.OK);
    }
}
