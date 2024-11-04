package com.iamnirvan.restaurant.core.models.requests.rules;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RuleCreateRequest {
    @NotBlank
    private String rule;
    @NotBlank
    private String ruleName;
    @NotBlank
    private String fact;
}
