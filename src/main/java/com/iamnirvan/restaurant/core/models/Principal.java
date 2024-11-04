package com.iamnirvan.restaurant.core.models;

import com.iamnirvan.restaurant.core.models.entities.Account;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@RequiredArgsConstructor
public class Principal {
    private final Account account;
    private final Object userDetails;
}
