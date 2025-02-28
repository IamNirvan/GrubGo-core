package com.iamnirvan.restaurant.core.models.requests.customer_allergen;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerAllergenCreateRequest {
    @NotBlank(message = "a valid name is required")
    private String name;
    @NotNull(message = "a valid customer id is required")
    @Min(value = 1, message = "invalid customer id")
    private Long customerId;
}
