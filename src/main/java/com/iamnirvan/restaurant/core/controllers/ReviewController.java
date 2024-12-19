package com.iamnirvan.restaurant.core.controllers;

import com.iamnirvan.restaurant.core.models.requests.review.ReviewCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.rules.RuleCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.rules.RuleUpdateRequest;
import com.iamnirvan.restaurant.core.models.responses.rules.RuleGetResponse;
import com.iamnirvan.restaurant.core.services.IReviewService;
import com.iamnirvan.restaurant.core.services.IRuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reviews")
public class ReviewController {
    private final IReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> createReview(@Valid @RequestBody List<ReviewCreateRequest> requests) {
        return ResponseEntity.ok(reviewService.createReview(requests));
    }

}
