package com.iamnirvan.restaurant.core.models.responses.customer;

import com.iamnirvan.restaurant.core.models.responses.user.AccountGetResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLoginResponse {
    private String token;
    private CustomerGetResponse userInfo;
}
