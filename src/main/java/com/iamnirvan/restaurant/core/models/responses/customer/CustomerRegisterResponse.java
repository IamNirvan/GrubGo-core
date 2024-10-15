package com.iamnirvan.restaurant.core.models.responses.customer;

import com.iamnirvan.restaurant.core.models.responses.user.AccountGetResponse;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class CustomerRegisterResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private AccountGetResponse accountInfo;
    private OffsetDateTime created;
}
