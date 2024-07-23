package com.iamnirvan.restaurant.core.models.responses.category;

import com.iamnirvan.restaurant.core.models.responses.dish.DishSimplifiedGetResponse;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class CategoryGetResponse {
    private Long id;
    private String name;
    private List<DishSimplifiedGetResponse> dishes;
    private String createdBy;
    private OffsetDateTime created;
    private String updatedBy;
    private OffsetDateTime updated;
}
