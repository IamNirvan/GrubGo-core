package com.iamnirvan.restaurant.core.controllers;

import com.iamnirvan.restaurant.core.services.IAddressService;
import com.iamnirvan.restaurant.core.services.ISentimentAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/analyse")
public class SentimentAnalysisController {
    private final ISentimentAnalysisService addressService;

    @PostMapping("/reviews/sentiment/dish/{id}")
    public ResponseEntity<?> analyseSentiment(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(addressService.analyseSentimentInDishReviews(id));
    }
}
