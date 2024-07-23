package com.iamnirvan.restaurant.core.models.requests.dish;

import com.iamnirvan.restaurant.core.models.requests.dish_portion.DishPortionCreateRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class DishCreateRequest {
    @NotBlank(message = "a valid name is required")
    private final String name;
    @NotBlank(message = "a description name is required")
    private final String description;
    @NotNull(message = "a valid list of dish portions is required")
    private List<DishPortionCreateRequest> dishPortion;
    @NotNull(message = "a valid list of category ids is required")
    private List<Long> categoryIds;
}
