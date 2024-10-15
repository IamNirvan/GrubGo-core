package com.iamnirvan.restaurant.core.models.responses.review;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class ReviewGetResponseWithoutDishId {
    private Long id;
    private String title;
    private String content;
    private Integer rating;
    private Long customerId;
    private OffsetDateTime created;
    private OffsetDateTime updated;
}
