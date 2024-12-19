package com.iamnirvan.restaurant.core.models.responses.dish_portion_cart;

import com.iamnirvan.restaurant.core.models.responses.dish_portion.DishPortionGetResponseWithoutDishName;
import com.iamnirvan.restaurant.core.models.responses.review.ReviewGetResponseWithoutDishId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DishPortionCartToReview {
    private OffsetDateTime orderDate;
    private Long dishPortionCartId;
    private Long dishId;
    private String name;
    private byte[] image;
}
