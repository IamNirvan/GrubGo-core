package com.iamnirvan.restaurant.core.services.impl;

import com.iamnirvan.restaurant.core.exceptions.BadRequestException;
import com.iamnirvan.restaurant.core.exceptions.ConflictException;
import com.iamnirvan.restaurant.core.exceptions.NotFoundException;
import com.iamnirvan.restaurant.core.models.entities.Account;
import com.iamnirvan.restaurant.core.models.entities.Employee;
import com.iamnirvan.restaurant.core.models.requests.employee.EmployeeRegisterRequest;
import com.iamnirvan.restaurant.core.models.requests.employee.EmployeeUpdateRequest;
import com.iamnirvan.restaurant.core.models.requests.user.AccountCreateRequest;
import com.iamnirvan.restaurant.core.models.responses.employee.EmployeeGetResponse;
import com.iamnirvan.restaurant.core.models.responses.employee.EmployeeRegisterResponse;
import com.iamnirvan.restaurant.core.models.responses.employee.EmployeeDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.employee.EmployeeUpdateResponse;
import com.iamnirvan.restaurant.core.repositories.EmployeeRepository;
import com.iamnirvan.restaurant.core.repositories.RoleRepository;
import com.iamnirvan.restaurant.core.services.IEmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmployeeService implements IEmployeeService {
    private final EmployeeRepository employeeRepository;
    private final AccountService accountService;
    private final RoleRepository roleRepository;

    /**
     * Registers a new employee.
     *
     * @param request the employee creation request containing employee details
     * @return the response containing the created employee's details
     * @throws ConflictException if there is a conflict while creating the account
     * @throws NotFoundException if the role specified in the request is not found
     * @throws BadRequestException if an error occurs while creating the account
     */
    @Override
    @Transactional
    public EmployeeRegisterResponse registerEmployee(EmployeeRegisterRequest request) {
        Account account;
        try {
            final AccountCreateRequest createRequest = new AccountCreateRequest();
            createRequest.setUsername(request.getUsername());
            createRequest.setPassword(request.getPassword());
            createRequest.setRoleId(roleRepository.findByName("ADMIN").getId());
            account = accountService.createAccount(createRequest);
        } catch (ConflictException | NotFoundException e) {
            log.error(e);
            throw e;
        } catch (Exception e) {
            log.error(e);
            throw new BadRequestException("An error occurred while creating account");
        }

        Employee employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .account(account)
                .created(OffsetDateTime.now())
                .build();

        employeeRepository.save(employee);
        log.debug("Employee created: {}", employee);

        return Parser.toEmployeeRegisterResponse(employee);
    }

    /**
     * Updates an existing employee.
     *
     * @param id the ID of the employee to update
     * @param request the employee update request containing updated details
     * @return the response containing the updated employee's details
     * @throws NotFoundException if the employee with the specified ID does not exist
     * @throws BadRequestException if an error occurs while updating the account or if the first name or last name is empty
     */
    @Override
    @Transactional
    public EmployeeUpdateResponse updateEmployee(Long id, EmployeeUpdateRequest request) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Employee with id %s does not exist", id)));

        // Update the account information
        try {
            if (request.getAccountInfo() != null) {
                accountService.updateAccount(employee.getAccount().getId(), request.getAccountInfo());
            }
        } catch (ConflictException | NotFoundException e) {
            log.error(e);
            throw e;
        } catch (Exception e) {
            log.error(e);
            throw new BadRequestException("An error occurred while updating the account");
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
        log.debug("Employee updated: {}", employee);

        return Parser.toEmployeeUpdateResponse(employee);
    }

    /**
     * Deletes an existing employee.
     *
     * @param id the ID of the employee to delete
     * @return the response containing the deleted employee's details
     * @throws NotFoundException if the employee with the specified ID does not exist
     */
    @Override
    public EmployeeDeleteResponse deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() ->
                new NotFoundException((String.format("Employee with id %s does not exist", id))));

        employeeRepository.delete(employee);
        log.debug("Employee deleted: {}", employee);

        return Parser.toEmployeeDeleteResponse(employee);
    }

    /**
     * Retrieves a list of employees or a specific employee by ID.
     *
     * @param id the ID of the employee to retrieve (optional)
     * @return a list of employee responses
     * @throws NotFoundException if the employee with the specified ID does not exist
     */
    @Override
    public List<EmployeeGetResponse> getAllEmployees(Long id) {
        if (id != null) {
            Employee employee = employeeRepository.findById(id).orElseThrow(() ->
                    new NotFoundException((String.format("Employee with id %s does not exist", id))));
            return List.of(Parser.toEmployeeGetResponse(employee));
        }
        return employeeRepository.findAll().stream().map(Parser::toEmployeeGetResponse).collect(Collectors.toList());
    }

    public static class Parser {
        public static EmployeeUpdateResponse toEmployeeUpdateResponse(@NotNull Employee employee) {
            return EmployeeUpdateResponse.builder()
                    .id(employee.getId())
                    .firstName(employee.getFirstName())
                    .lastName(employee.getLastName())
                    .accountInfo(AccountService.Parser.toAccountUpdateResponse(employee.getAccount()))
                    .updated(employee.getUpdated())
                    .build();
        }

        public static EmployeeRegisterResponse toEmployeeRegisterResponse(@NotNull Employee employee) {
            return EmployeeRegisterResponse.builder()
                    .id(employee.getId())
                    .firstName(employee.getFirstName())
                    .lastName(employee.getLastName())
                    .accountInfo(AccountService.Parser.toAccountGetResponse(employee.getAccount()))
                    .created(employee.getCreated())
                    .build();
        }

        public static EmployeeDeleteResponse toEmployeeDeleteResponse(@NotNull Employee employee) {
            return EmployeeDeleteResponse.builder()
                    .id(employee.getId())
                    .firstName(employee.getFirstName())
                    .lastName(employee.getLastName())
                    .build();
        }

        public static EmployeeGetResponse toEmployeeGetResponse(@NotNull Employee employee) {
            return EmployeeGetResponse.builder()
                    .id(employee.getId())
                    .firstName(employee.getFirstName())
                    .lastName(employee.getLastName())
                    .accountInfo(AccountService.Parser.toAccountGetResponse(employee.getAccount()))
                    .created(employee.getCreated())
                    .updated(employee.getUpdated())
                    .build();
        }
    }
}
