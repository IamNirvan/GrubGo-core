package com.iamnirvan.restaurant.core.models.responses.review;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class ReviewDeleteResponse {
    private Long id;
    private String title;
    private String content;
    private Integer rating;
    private OffsetDateTime created;
    private OffsetDateTime updated;
}
