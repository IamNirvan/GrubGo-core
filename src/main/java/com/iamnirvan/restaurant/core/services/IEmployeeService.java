package com.iamnirvan.restaurant.core.services;

import com.iamnirvan.restaurant.core.models.requests.employee.EmployeeRegisterRequest;
import com.iamnirvan.restaurant.core.models.requests.employee.EmployeeUpdateRequest;
import com.iamnirvan.restaurant.core.models.responses.employee.EmployeeGetResponse;
import com.iamnirvan.restaurant.core.models.responses.employee.EmployeeRegisterResponse;
import com.iamnirvan.restaurant.core.models.responses.employee.EmployeeDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.employee.EmployeeUpdateResponse;

import java.util.List;

public interface IEmployeeService {
    EmployeeRegisterResponse registerEmployee(EmployeeRegisterRequest request);
    EmployeeUpdateResponse updateEmployee(Long id, EmployeeUpdateRequest request);
    EmployeeDeleteResponse deleteEmployee(Long id);
    List<EmployeeGetResponse> getAllEmployees(Long id);
}
