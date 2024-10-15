package com.iamnirvan.restaurant.core.models.responses.employee;

import com.iamnirvan.restaurant.core.models.responses.user.AccountGetResponse;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class EmployeeGetResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private AccountGetResponse accountInfo;
    private OffsetDateTime created;
    private OffsetDateTime updated;
}
