package com.iamnirvan.restaurant.core.models.responses.dish_portion_cart;

import java.time.OffsetDateTime;

public interface DishPortionCartToReviewProjection {
    OffsetDateTime getOrderDate();
    Long getDishPortionCartId();
    Long getDishId();
    String getName();
    byte[] getImage();
}
