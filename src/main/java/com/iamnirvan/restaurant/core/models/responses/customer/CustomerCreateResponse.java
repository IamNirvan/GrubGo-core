package com.iamnirvan.restaurant.core.models.responses.customer;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class CustomerCreateResponse {
    private String firstName;
    private String lastName;
    private OffsetDateTime created;
}
