package com.iamnirvan.restaurant.core.controllers;

import com.iamnirvan.restaurant.core.enums.EFoodOrderStatus;
import com.iamnirvan.restaurant.core.models.requests.food_order.FoodOrderCreateRequest;
import com.iamnirvan.restaurant.core.models.responses.food_order.FoodOrderGetResponse;
import com.iamnirvan.restaurant.core.services.IFoodOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
    public ResponseEntity<?> createOrders(@Valid @RequestBody List<FoodOrderCreateRequest> requests) {
        return new ResponseEntity<>(foodOrderService.placeOrder(requests), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getOrders(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "cusId", required = false) Long cusId
    ) {
        final List<FoodOrderGetResponse> response = foodOrderService.getOrders(id, cusId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable(value = "id", required = true) Long id,
            @RequestParam(value = "status", required = true) EFoodOrderStatus status
    ) {
        return new ResponseEntity<>(foodOrderService.updateOrderStatus(id, status), HttpStatus.OK);
    }
}
