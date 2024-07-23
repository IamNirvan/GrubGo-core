package com.iamnirvan.restaurant.core.models.requests.dish_portion;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DishPortionCreateRequest {
    @NotNull(message = "price is required")
    @Min(value = 1, message = "price must be greater than 0")
    private Double price;
    @NotNull(message = "Portion id is required")
    @Min(value = 1, message = "A valid portion id is required")
    private Long portionId;
}
