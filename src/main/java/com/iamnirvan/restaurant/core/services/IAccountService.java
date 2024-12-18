package com.iamnirvan.restaurant.core.services;

import com.iamnirvan.restaurant.core.models.entities.Account;
import com.iamnirvan.restaurant.core.models.requests.login.LoginRequest;
import com.iamnirvan.restaurant.core.models.requests.user.AccountCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.user.AccountUpdateRequest;
import com.iamnirvan.restaurant.core.models.responses.customer.CustomerLoginResponse;
import com.iamnirvan.restaurant.core.models.responses.employee.EmployeeLoginResponse;
import com.iamnirvan.restaurant.core.models.responses.user.UserDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.user.AccountGetResponse;
import com.iamnirvan.restaurant.core.models.responses.user.AccountUpdateResponse;

/**
 * This is only used for internal purposes only... These functions will not be directly bound to APIS..
 */
public interface IAccountService {
    Account createAccount(AccountCreateRequest request);
    AccountUpdateResponse updateAccount(Long id, AccountUpdateRequest request);
    AccountGetResponse getAccount(Long id);
    UserDeleteResponse deleteAccount(Long id);
    CustomerLoginResponse customerLogin(LoginRequest loginRequest);
    EmployeeLoginResponse employeeLogin(LoginRequest loginRequest);
}
