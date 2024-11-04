package com.iamnirvan.restaurant.core.services.impl;

import com.iamnirvan.restaurant.core.exceptions.ConflictException;
import com.iamnirvan.restaurant.core.exceptions.NotFoundException;
import com.iamnirvan.restaurant.core.models.TokenPayload;
import com.iamnirvan.restaurant.core.models.entities.*;
import com.iamnirvan.restaurant.core.models.requests.login.LoginRequest;
import com.iamnirvan.restaurant.core.models.requests.user.AccountCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.user.AccountUpdateRequest;
import com.iamnirvan.restaurant.core.models.responses.customer.CustomerTokenDataGetResponse;
import com.iamnirvan.restaurant.core.models.responses.employee.EmployeeTokenDataGetResponse;
import com.iamnirvan.restaurant.core.models.responses.user.UserDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.user.AccountGetResponse;
import com.iamnirvan.restaurant.core.models.responses.user.AccountUpdateResponse;
import com.iamnirvan.restaurant.core.repositories.CustomerRepository;
import com.iamnirvan.restaurant.core.repositories.EmployeeRepository;
import com.iamnirvan.restaurant.core.repositories.RoleRepository;
import com.iamnirvan.restaurant.core.repositories.AccountRepository;
import com.iamnirvan.restaurant.core.services.IAccountService;
import com.iamnirvan.restaurant.core.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AccountService implements IAccountService {
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AccountService(AccountRepository accountRepository, RoleRepository roleRepository, AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          CustomerRepository customerRepository,
                          EmployeeRepository employeeRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(10);
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Account createAccount(@NotNull AccountCreateRequest request) {
        if (accountRepository.existsUserByUsername(request.getUsername())) {
            throw new ConflictException("Username is taken");
        }

        final Roles role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new NotFoundException("Role not found"));

        Account account = Account.builder()
                .roles(role)
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .active(true)
                .build();

        accountRepository.save(account);
        log.debug("Created account: {}", account);
        return account;
    }

    @Override
    @Transactional
    public AccountUpdateResponse updateAccount(Long id, @NotNull AccountUpdateRequest request) {
        final Account account = accountRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found"));

        if (request.getUsername() != null) {
            if (accountRepository.existsUserByUsernameAndId(request.getUsername(), id)) {
                throw new ConflictException("Username is taken");
            }
            account.setUsername(request.getUsername());
        }

        if (request.getPassword() != null) {
            account.setPassword(request.getPassword());
        }

        if (request.getActive() != null) {
            account.setActive(request.getActive());
        }

        log.debug("Updated account: {}", account);
        accountRepository.save(account);

        return Parser.toAccountUpdateResponse(account);
    }

    @Override
    public AccountGetResponse getAccount(Long id) {
        final Account account = accountRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found"));

        return Parser.toAccountGetResponse(account);
    }

    @Override
    public UserDeleteResponse deleteAccount(Long id) {
        final Account account = accountRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found"));

        accountRepository.delete(account);
        log.debug("Deleted user: {}", account);

        return Parser.toAccountDeleteResponse(account);
    }

    @Override
    public String login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        if (authentication.isAuthenticated()) {
            Account account = accountRepository.findAccountByUsername(loginRequest.getUsername()).orElseThrow(
                    () -> new NotFoundException("User not found")
            );

            Object userDetails = null;
            CustomerTokenDataGetResponse customer = customerRepository.findCustomerByAccountId(account.getId());
            if (customer != null) {
                userDetails = customer;
            }
            else {
                EmployeeTokenDataGetResponse employee = employeeRepository.findEmployeeByAccountId(account.getId());
                if (employee != null) {
                    userDetails = employee;
                }
            }

            if (userDetails == null) {
                throw new NotFoundException("User not found");
            }

//            return jwtUtil.generateToken(loginRequest.getUsername());
            TokenPayload principal = TokenPayload.builder()
                    .account(AccountService.Parser.toAccountGetResponse(account))
                    .userDetails(userDetails)
                    .build();

            return jwtUtil.generateToken(loginRequest.getUsername(), principal);
        }
        return null;
    }

    public static class Parser {
        public static AccountGetResponse toAccountGetResponse(@NotNull Account account) {
            final Roles role = account.getRoles();

            return AccountGetResponse.builder()
                    .id(account.getId())
                    .username(account.getUsername())
                    .roleId(role.getId())
                    .roleName(role.getName())
                    .active(account.getActive())
                    .build();
        }

        public static UserDeleteResponse toAccountDeleteResponse(@NotNull Account account) {
            return UserDeleteResponse.builder()
                    .id(account.getId())
                    .username(account.getUsername())
                    .build();
        }

        public static AccountUpdateResponse toAccountUpdateResponse(@NotNull Account account) {
            return AccountUpdateResponse.builder()
                    .id(account.getId())
                    .username(account.getUsername())
                    .active(account.getActive())
                    .build();
        }
    }
}
