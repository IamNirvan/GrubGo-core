package com.iamnirvan.restaurant.core.models.requests.dish;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class DishUpdateRequest {
    @NotNull(message = "dish id is required")
    @Min(value = 1, message = "dish id must be greater than 0")
    private final Long id;
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "dish name must contain only alphanumeric characters and spaces")
    private final String name;
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "dish description must contain only alphanumeric characters and spaces")
    private final String description;
    // FIXME: add image too...
}
