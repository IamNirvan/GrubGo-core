package com.iamnirvan.restaurant.core.models;

import com.iamnirvan.restaurant.core.models.entities.Account;
import com.iamnirvan.restaurant.core.models.responses.user.AccountGetResponse;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@RequiredArgsConstructor
public class TokenPayload {
    private final AccountGetResponse account;
    private final Object userDetails;
}
