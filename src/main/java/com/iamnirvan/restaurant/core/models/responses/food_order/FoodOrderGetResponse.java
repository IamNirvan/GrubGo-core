package com.iamnirvan.restaurant.core.models.responses.food_order;

import com.iamnirvan.restaurant.core.enums.EStatus;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class FoodOrderGetResponse {
    private Long id;
    private String notes;
    private EStatus status;
    private Double total;
    private OffsetDateTime date;
}
