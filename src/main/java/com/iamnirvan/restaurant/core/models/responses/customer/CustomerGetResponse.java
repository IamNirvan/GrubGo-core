package com.iamnirvan.restaurant.core.models.responses.customer;

import com.iamnirvan.restaurant.core.models.responses.user.AccountGetResponse;
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
    private AccountGetResponse account;
    private OffsetDateTime created;
    private OffsetDateTime updated;
}
