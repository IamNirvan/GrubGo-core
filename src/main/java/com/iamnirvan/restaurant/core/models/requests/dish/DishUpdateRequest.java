package com.iamnirvan.restaurant.core.models.requests.dish;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DishUpdateRequest {
    @NotNull
    @Min(1)
    private final Long id;
    private final String name;
    private final String description;
}
