package com.iamnirvan.restaurant.core.models.responses.employee;

import com.iamnirvan.restaurant.core.models.responses.customer.CustomerGetResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeLoginResponse {
    private String token;
    private EmployeeGetResponse userInfo;
}
