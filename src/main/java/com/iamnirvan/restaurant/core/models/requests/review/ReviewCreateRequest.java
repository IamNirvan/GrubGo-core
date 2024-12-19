package com.iamnirvan.restaurant.core.models.requests.review;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewCreateRequest {
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Content is required")
    private String content;
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be greater than 0")
    private Integer rating;
    @NotNull(message = "Customer id is required")
    @Min(value = 1, message = "A valid customer id is required")
    private Long customerId;
    @NotNull(message = "Dish id is required")
    @Min(value = 1, message = "A valid dish id is required")
    private Long dishId;
    @NotNull(message = "Dish id is required")
    @Min(value = 1, message = "A valid dish portion cart id is required")
    private Long dishPortionCartId;
}
