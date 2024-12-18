package com.iamnirvan.restaurant.core.models.responses.dish_portion;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DishPortionGetResponseWithoutDishName {
    private Long id;
    private String portionName;
    private Double price;
}
