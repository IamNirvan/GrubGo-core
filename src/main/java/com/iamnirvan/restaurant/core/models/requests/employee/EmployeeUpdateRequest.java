package com.iamnirvan.restaurant.core.models.requests.employee;

import com.iamnirvan.restaurant.core.models.requests.user.AccountUpdateRequest;
import lombok.Data;

@Data
public class EmployeeUpdateRequest {
    private AccountUpdateRequest accountInfo;
    private String firstName;
    private String lastName;
}
