package com.iamnirvan.restaurant.core.services.impl;

import com.iamnirvan.restaurant.core.exceptions.BadRequestException;
import com.iamnirvan.restaurant.core.exceptions.NotFoundException;
import com.iamnirvan.restaurant.core.models.entities.Employee;
import com.iamnirvan.restaurant.core.models.requests.employee.EmployeeCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.employee.EmployeeUpdateRequest;
import com.iamnirvan.restaurant.core.models.responses.employee.EmployeeCreateResponse;
import com.iamnirvan.restaurant.core.models.responses.employee.EmployeeDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.employee.EmployeeUpdateResponse;
import com.iamnirvan.restaurant.core.repositories.EmployeeRepository;
import com.iamnirvan.restaurant.core.services.IEmployeeService;
import com.iamnirvan.restaurant.core.util.PasswordVerification;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmployeeService implements IEmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordVerification passwordVerification;

    @Override
    public EmployeeCreateResponse createEmployee(EmployeeCreateRequest request) {

        if (employeeRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        if (!passwordVerification.verifyPassword(request.getPassword())) {
            throw new BadRequestException("Password must contain at least 8 characters, one uppercase letter," +
                    " one lowercase letter, one number and one special character");
        }

        Employee employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .password(request.getPassword())
//                .designation(EDesignation.ADMIN)
                .created(OffsetDateTime.now())
                .build();

        employeeRepository.save(employee);
        log.debug(String.format("Employee created: %s", employee));

        return EmployeeCreateResponse.builder()
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .username(employee.getUsername())
                .password(employee.getPassword())
                .created(employee.getCreated())
                .build();
    }

    @Override
    @Transactional
    public EmployeeUpdateResponse updateEmployee(Long id, EmployeeUpdateRequest request) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Employee with id %s does not exist", id)));

        if (request.getPassword() != null) {
            if (!passwordVerification.verifyPassword(request.getPassword())) {
                throw new BadRequestException("Password must contain at least 8 characters, one uppercase letter," +
                        " one lowercase letter, one number and one special character");
            }
            employee.setPassword(request.getPassword());
        }

        if (request.getFirstName() != null) {
            if (request.getFirstName().isEmpty()) {
                throw new BadRequestException("First name cannot be empty");
            }
            employee.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            if(request.getLastName().isEmpty()) {
                throw new BadRequestException("Last name cannot be empty");
            }
            employee.setLastName(request.getLastName());
        }

        employee.setUpdated(OffsetDateTime.now());
        employeeRepository.save(employee);
        log.debug(String.format("Employee updated: %s", employee));

        return EmployeeUpdateResponse.builder()
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .password(employee.getPassword())
                .updated(employee.getUpdated())
                .build();
    }

    @Override
    public EmployeeDeleteResponse deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() ->
                new NotFoundException((String.format("Employee with id %s does not exist", id))));

        employeeRepository.delete(employee);
        log.debug(String.format("Employee deleted: %s", employee));

        return EmployeeDeleteResponse.builder()
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .username(employee.getUsername())
//                .designation(employee.getDesignation())
                .build();
    }

    @Override
    public List<Employee> getAllEmployees(Long id) {
        if (id != null) {
            Employee employee = employeeRepository.findById(id).orElseThrow(() ->
                    new NotFoundException((String.format("Employee with id %s does not exist", id))));
            return List.of(employee);
        }
        return employeeRepository.findAll();
    }

}
