package com.iamnirvan.restaurant.core.models.requests.food_order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FoodOrderCreateRequest {
    private String notes;
    @NotNull(message = "Cart id is required")
    @Min(value = 1, message = "A valid cart id is required")
    private Long cartId;
}
