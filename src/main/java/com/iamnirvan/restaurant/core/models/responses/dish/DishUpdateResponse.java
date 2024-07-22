package com.iamnirvan.restaurant.core.models.responses.dish;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class DishUpdateResponse {
    private Long id;
    private String name;
    private String description;
    private String createdBy;
    private OffsetDateTime created;
    private String updatedBy;
    private OffsetDateTime updated;
}
