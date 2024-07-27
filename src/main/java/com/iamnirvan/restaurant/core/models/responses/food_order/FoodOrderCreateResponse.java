package com.iamnirvan.restaurant.core.models.responses.food_order;

import com.iamnirvan.restaurant.core.enums.EStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class FoodOrderCreateResponse {
    private String notes;
    @NotNull(message = "Cart id is required")
    @Min(value = 1, message = "A valid cart id is required")
    private Long cartId;
    private OffsetDateTime date;
    private EStatus status;
    private Double total;
}
