package com.iamnirvan.restaurant.core.models.requests.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * When removing a dish portion (dish and its portion) from the cart, provide the dish portion cart id...
 * (primary key of the associative entity)
 * */

@Data
public class RemoveDishFromCartRequest {
    @NotNull(message = "Dish portion cart id is required")
    @Min(value = 1, message = "Dish portion cart id must be greater than 0")
    private Long dishPortionCartId;
}
