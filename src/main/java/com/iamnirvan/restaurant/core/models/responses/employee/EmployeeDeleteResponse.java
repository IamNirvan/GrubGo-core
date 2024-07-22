package com.iamnirvan.restaurant.core.models.responses.employee;

import com.iamnirvan.restaurant.core.enums.EDesignation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeDeleteResponse {
    private String firstName;
    private String lastName;
    private String username;
    private EDesignation designation;
}
