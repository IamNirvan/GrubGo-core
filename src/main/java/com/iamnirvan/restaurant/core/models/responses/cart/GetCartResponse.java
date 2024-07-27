package com.iamnirvan.restaurant.core.models.responses.cart;

import com.iamnirvan.restaurant.core.models.responses.dish_portion.DishPortionGetResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetCartResponse {
    private Long id;
    private List<DishPortionGetResponse> dishes;
}
