package com.iamnirvan.restaurant.core.models.requests.rules.evaluation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EvaluateRulesRequest {
    @NotNull(message = "customerId is required")
    @Min(value = 1, message = "customerId must be greater than 0")
    private Long customerId;
    @NotNull(message = "dishId is required")
    @Min(value = 1, message = "dishId must be greater than 0")
    private Long dishId;
}
