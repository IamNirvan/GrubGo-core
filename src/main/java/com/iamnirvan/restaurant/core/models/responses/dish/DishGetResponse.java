package com.iamnirvan.restaurant.core.models.responses.dish;

import com.iamnirvan.restaurant.core.models.responses.dish_portion.DishPortionGetResponseWithoutDishName;
import com.iamnirvan.restaurant.core.models.responses.review.ReviewGetResponseWithoutDishId;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Set;

@Data
@Builder
public class DishGetResponse {
    private Long id;
    private String name;
    private String description;
    private Set<DishPortionGetResponseWithoutDishName> dishPortions;
    private Set<ReviewGetResponseWithoutDishId> reviews;
    private byte[] image;
    private OffsetDateTime created;
    private OffsetDateTime updated;
}
