package com.iamnirvan.restaurant.core.models.responses.customer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDeleteResponse {
    private Long id;
    private String firstName;
    private String lastName;
}
