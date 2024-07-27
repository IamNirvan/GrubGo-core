package com.iamnirvan.restaurant.core.controllers;

import com.iamnirvan.restaurant.core.models.requests.food_order.FoodOrderCreateRequest;
import com.iamnirvan.restaurant.core.services.IFoodOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/order")
public class FoodOrderController {
    private final IFoodOrderService foodOrderService;

    @PostMapping
    public ResponseEntity<?> createPortion(@Valid @RequestBody List<FoodOrderCreateRequest> requests) {
        return new ResponseEntity<>(foodOrderService.placeOrder(requests), HttpStatus.CREATED);
    }
}
