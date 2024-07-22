package com.iamnirvan.restaurant.core.models.responses.customer_allergen;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerAllergenDeleteResponse {
    private Long id;
    private String name;
}
