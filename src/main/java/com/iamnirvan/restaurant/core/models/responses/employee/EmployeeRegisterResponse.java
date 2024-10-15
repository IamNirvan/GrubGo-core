package com.iamnirvan.restaurant.core.models.responses.employee;

import com.iamnirvan.restaurant.core.models.entities.Account;
import com.iamnirvan.restaurant.core.models.responses.user.AccountGetResponse;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class EmployeeRegisterResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private AccountGetResponse accountInfo;
    private OffsetDateTime created;
}
