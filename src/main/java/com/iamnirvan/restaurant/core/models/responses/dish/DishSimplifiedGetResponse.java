package com.iamnirvan.restaurant.core.models.responses.dish;

import com.iamnirvan.restaurant.core.models.entities.DishImage;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class DishSimplifiedGetResponse {
    private Long id;
    private String name;
    private String description;
    private Set<DishImage> images;
}
