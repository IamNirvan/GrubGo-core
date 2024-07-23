package com.iamnirvan.restaurant.core.models.responses.dish;

import com.iamnirvan.restaurant.core.models.entities.DishImage;
import com.iamnirvan.restaurant.core.models.entities.DishPortion;
import com.iamnirvan.restaurant.core.models.entities.Review;
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
    private Set<DishPortion> dishPortions;
    private Set<DishImage> images;
    private Set<Review> reviews;
    private String createdBy;
    private OffsetDateTime created;
    private String updatedBy;
    private OffsetDateTime updated;
}
