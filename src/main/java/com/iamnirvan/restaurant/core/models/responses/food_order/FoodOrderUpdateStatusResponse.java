package com.iamnirvan.restaurant.core.models.responses.food_order;

import com.iamnirvan.restaurant.core.enums.EFoodOrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class FoodOrderUpdateStatusResponse {
    private Long id;
    private EFoodOrderStatus status;
    private OffsetDateTime date;
}
