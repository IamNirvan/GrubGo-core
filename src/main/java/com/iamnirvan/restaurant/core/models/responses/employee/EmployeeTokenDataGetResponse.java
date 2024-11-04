package com.iamnirvan.restaurant.core.models.responses.employee;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class EmployeeTokenDataGetResponse {
    private Long id;
    private String firstName;
    private String lastName;
}
