package com.iamnirvan.restaurant.core.models.responses.dish;

import com.iamnirvan.restaurant.core.models.responses.dish_portion.DishPortionGetResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Deprecated
public class DishWithPortionGetResponse {
    private Long id;
    private String name;
    private String description;
    private DishPortionGetResponse portion;
}
