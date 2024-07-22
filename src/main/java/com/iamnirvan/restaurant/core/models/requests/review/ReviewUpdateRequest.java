package com.iamnirvan.restaurant.core.models.requests.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewUpdateRequest {
    @NotNull(message = "Id is required")
    @Min(value = 1, message = "A valid Id is required")
    private Long id;
    private String title;
    private String content;
    @Max(value = 5, message = "Rating must be less than or equal to 5")
    @Min(value = 1, message = "Rating must be greater than 0")
    private Integer rating;
}
