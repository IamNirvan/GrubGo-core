package com.iamnirvan.restaurant.core.models.requests.employee;

import lombok.Data;

@Data
public class EmployeeUpdateRequest {
    private String password;
    private String firstName;
    private String lastName;
}
