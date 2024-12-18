package com.iamnirvan.restaurant.core.controllers;

import com.iamnirvan.restaurant.core.models.requests.dish.DishCreateRequestList;
import com.iamnirvan.restaurant.core.models.requests.dish.DishUpdateRequestList;
import com.iamnirvan.restaurant.core.models.responses.dish.DishGetResponse;
import com.iamnirvan.restaurant.core.services.IDishService;
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
    public ResponseEntity<?> getDishes(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "includeImg", required = false) boolean includeImg) {
        final List<DishGetResponse> response = dishService.getDishes(id, includeImg);
        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/review/pending")
    public ResponseEntity<?> getDishesToBeReviewed(@RequestParam(value = "customerId") Long customerId) {
        final List<DishGetResponse> response = dishService.getDishesToBeReviewed(customerId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createDishes(@ModelAttribute DishCreateRequestList payload) {
        return new ResponseEntity<>(dishService.createDish(payload.getRequests()), HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<?> updateDishes(@ModelAttribute DishUpdateRequestList payload) {
        return new ResponseEntity<>(dishService.updateDish(payload.getRequests()), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteDishes(@RequestParam(value = "ids") List<Long> ids) {
        return new ResponseEntity<>(dishService.deleteDish(ids), HttpStatus.OK);
    }

    @GetMapping("/metrics/{id}")
    public ResponseEntity<?> getDishMetrics(@PathVariable(value = "id") Long dishId) {
        return new ResponseEntity<>(dishService.getDishMetrics(dishId), HttpStatus.OK);
    }
}
