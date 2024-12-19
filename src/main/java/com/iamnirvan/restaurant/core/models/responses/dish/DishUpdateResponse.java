package com.iamnirvan.restaurant.core.models.responses.dish;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class DishUpdateResponse {
    private Long id;
    private String name;
    private String description;
    private List<String> ingredients;
    private OffsetDateTime created;
    private OffsetDateTime updated;
}
