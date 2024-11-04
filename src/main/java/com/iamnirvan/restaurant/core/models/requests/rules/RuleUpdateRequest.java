package com.iamnirvan.restaurant.core.models.requests.rules;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RuleUpdateRequest {
    @NotNull(message = "Id is required")
    @Min(value = 1, message = "Id must be greater than 0")
    private Long id;
    private String rule;
    private String ruleName;
}
