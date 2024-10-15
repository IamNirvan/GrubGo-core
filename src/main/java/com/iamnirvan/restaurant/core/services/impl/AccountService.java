package com.iamnirvan.restaurant.core.services.impl;

import com.iamnirvan.restaurant.core.exceptions.ConflictException;
import com.iamnirvan.restaurant.core.exceptions.NotFoundException;
import com.iamnirvan.restaurant.core.models.entities.*;
import com.iamnirvan.restaurant.core.models.requests.user.AccountCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.user.AccountUpdateRequest;
import com.iamnirvan.restaurant.core.models.responses.user.UserDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.user.AccountGetResponse;
import com.iamnirvan.restaurant.core.models.responses.user.AccountUpdateResponse;
import com.iamnirvan.restaurant.core.repositories.RoleRepository;
import com.iamnirvan.restaurant.core.repositories.AccountRepository;
import com.iamnirvan.restaurant.core.services.IAccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class AccountService implements IAccountService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    @Override
    public Account createAccount(@NotNull AccountCreateRequest request) {
        if (accountRepository.existsUserByUsername(request.getUsername())) {
            throw new ConflictException("Username is taken");
        }

        final Roles role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new NotFoundException("Role not found"));

        if (role.getName().equalsIgnoreCase("admin")) {
            throw new ConflictException("Cannot create an admin user");
        }

        Account account = Account.builder()
                .roles(role)
                .username(request.getUsername())
                .password(request.getPassword())
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
