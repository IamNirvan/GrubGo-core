package com.iamnirvan.restaurant.core.controllers;

import com.iamnirvan.restaurant.core.models.requests.rules.RuleCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.rules.RuleUpdateRequest;
import com.iamnirvan.restaurant.core.models.responses.rules.RuleGetResponse;
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
@RequestMapping("/v1/rules")
public class RuleController {
    private final IRuleService ruleService;

    @GetMapping
    public ResponseEntity<?> getRules(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "fact", required = false) String fact) {
        final List<RuleGetResponse> response = ruleService.getRules(id, fact);
        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(ruleService.getRules(id, fact));
    }

    @PostMapping
    public ResponseEntity<?> createRule(@Valid @RequestBody List<RuleCreateRequest> requests) {
        return ResponseEntity.ok(ruleService.createRule(requests));
    }

    @PutMapping
    public ResponseEntity<?> updateRule(@Valid @RequestBody List<RuleUpdateRequest> requests) {
        return ResponseEntity.ok(ruleService.updateRule(requests));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteRule(@RequestParam(value = "ids") List<Long> ids) {
        return ResponseEntity.ok(ruleService.deleteRule(ids));
    }
}
