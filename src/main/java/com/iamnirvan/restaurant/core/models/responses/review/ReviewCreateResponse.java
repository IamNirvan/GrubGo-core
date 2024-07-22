package com.iamnirvan.restaurant.core.models.responses.review;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class ReviewCreateResponse {
    private Long id;
    private String title;
    private String content;
    private Integer rating;
    private Long customerId;
    private Long dishId;
    private OffsetDateTime created;
    private OffsetDateTime updated;
}
