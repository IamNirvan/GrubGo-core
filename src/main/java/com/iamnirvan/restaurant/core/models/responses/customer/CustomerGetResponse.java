package com.iamnirvan.restaurant.core.models.responses.customer;

import com.iamnirvan.restaurant.core.models.responses.address.AddressGetResponse;
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
    private AddressGetResponse address;
    private Long cartId;
    private OffsetDateTime created;
    private OffsetDateTime updated;
}
