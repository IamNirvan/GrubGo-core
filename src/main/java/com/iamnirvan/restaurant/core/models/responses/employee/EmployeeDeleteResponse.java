package com.iamnirvan.restaurant.core.models.responses.employee;

import com.iamnirvan.restaurant.core.enums.EDesignation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeDeleteResponse {
    private Long id;
    private String firstName;
    private String lastName;
}
