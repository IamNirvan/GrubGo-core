package com.iamnirvan.restaurant.core.services;

import com.iamnirvan.restaurant.core.models.entities.Employee;
import com.iamnirvan.restaurant.core.models.requests.employee.EmployeeCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.employee.EmployeeUpdateRequest;
import com.iamnirvan.restaurant.core.models.responses.employee.EmployeeCreateResponse;
import com.iamnirvan.restaurant.core.models.responses.employee.EmployeeDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.employee.EmployeeUpdateResponse;

import java.util.List;

public interface IEmployeeService {
    EmployeeCreateResponse createEmployee(EmployeeCreateRequest request);

    EmployeeUpdateResponse updateEmployee(Long id, EmployeeUpdateRequest request);

    EmployeeDeleteResponse deleteEmployee(Long id);

    List<Employee> getAllEmployees(Long id);

}
