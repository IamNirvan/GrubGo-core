package com.iamnirvan.restaurant.core.models.requests.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemsRequest {
    @NotNull(message = "Dish portion id is required")
    @Min(value = 1, message = "Dish portion id must be greater than 0")
    private Long dishPortionId;
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;
}
