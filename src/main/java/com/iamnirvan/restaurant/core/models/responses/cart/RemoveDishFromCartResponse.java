package com.iamnirvan.restaurant.core.models.responses.cart;

import com.iamnirvan.restaurant.core.models.responses.dish_portion.DishPortionGetResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RemoveDishFromCartResponse {
    private Long dishPortionCartId;
    private DishPortionGetResponse dishPortion;
}
