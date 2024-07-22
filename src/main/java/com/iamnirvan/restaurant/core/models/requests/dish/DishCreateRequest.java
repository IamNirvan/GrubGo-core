package com.iamnirvan.restaurant.core.models.requests.dish;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DishCreateRequest {
    @NotBlank(message = "a valid name is required")
    private final String name;
    @NotBlank(message = "a description name is required")
    private final String description;
}
