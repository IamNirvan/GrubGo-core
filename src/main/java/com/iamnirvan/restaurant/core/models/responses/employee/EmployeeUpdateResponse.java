package com.iamnirvan.restaurant.core.models.responses.employee;

import com.iamnirvan.restaurant.core.models.responses.user.AccountUpdateResponse;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class EmployeeUpdateResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private AccountUpdateResponse accountInfo;
    private OffsetDateTime updated;
}
