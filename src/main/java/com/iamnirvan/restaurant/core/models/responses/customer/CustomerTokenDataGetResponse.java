package com.iamnirvan.restaurant.core.models.responses.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTokenDataGetResponse {
    private Long id;
    private String firstName;
    private String lastName;
}
