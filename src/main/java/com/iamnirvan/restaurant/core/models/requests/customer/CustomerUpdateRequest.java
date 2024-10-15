package com.iamnirvan.restaurant.core.models.requests.customer;

import com.iamnirvan.restaurant.core.models.requests.user.AccountUpdateRequest;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerUpdateRequest {
    private AccountUpdateRequest accountInfo;
    private String firstName;
    private String lastName;
}
