package com.iamnirvan.restaurant.core.models.responses.dish;

import com.iamnirvan.restaurant.core.models.responses.dish_portion.DishPortionGetResponseWithoutDishName;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class DishCreateResponse {
    private Long id;
    private String name;
    private String description;
    private List<String> ingredients;
    private List<DishPortionGetResponseWithoutDishName> portions;
    private OffsetDateTime created;
    private OffsetDateTime updated;
}
