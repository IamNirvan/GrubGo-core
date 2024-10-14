package com.iamnirvan.restaurant.core.models.responses.customer;

import lombok.*;

import java.time.OffsetDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerGetResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private OffsetDateTime updated;
}
