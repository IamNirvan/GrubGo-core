package com.iamnirvan.restaurant.core.models.responses.dish_portion;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DishPortionGetResponse {
    private Long id;
    private String dishName;
    private String portionName;
    private Double price;
    private Integer quantity;
}
