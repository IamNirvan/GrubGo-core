package com.iamnirvan.restaurant.core.controllers;

import com.iamnirvan.restaurant.core.models.requests.cart.AddDishIntoCartRequest;
import com.iamnirvan.restaurant.core.services.ICartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/cart")
public class CartController {
    private final ICartService cartService;

    @PatchMapping("/{id}")
    public ResponseEntity<?> manipulateCartContent(
            @PathVariable("id") Long id,
            @Valid @RequestBody List<AddDishIntoCartRequest> requests,
            @RequestParam(value = "overrideQuantity", required = false) boolean overrideQuantity
    ) {
        return new ResponseEntity<>(cartService.manipulateCartContent(id, requests, overrideQuantity), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCartContent(@PathVariable("id") Long id) {
        return new ResponseEntity<>(cartService.getCartContent(id), HttpStatus.OK);
    }
}
