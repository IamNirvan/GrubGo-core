package com.iamnirvan.restaurant.core.models.requests.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryCreateRequest {
    @NotBlank
    private String name;
}
