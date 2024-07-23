package com.iamnirvan.restaurant.core.models.responses.category;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class CategoryDeleteResponse {
    private Long id;
    private String name;
    private String createdBy;
    private OffsetDateTime created;
    private String updatedBy;
    private OffsetDateTime updated;
}
