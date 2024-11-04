package com.iamnirvan.restaurant.core.services;

import com.iamnirvan.restaurant.core.models.Principal;
import com.iamnirvan.restaurant.core.models.UserPrincipal;
import com.iamnirvan.restaurant.core.models.entities.Account;
import com.iamnirvan.restaurant.core.models.responses.customer.CustomerTokenDataGetResponse;
import com.iamnirvan.restaurant.core.models.responses.employee.EmployeeTokenDataGetResponse;
import com.iamnirvan.restaurant.core.repositories.AccountRepository;
import com.iamnirvan.restaurant.core.repositories.CustomerRepository;
import com.iamnirvan.restaurant.core.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public UserDetailsService(AccountRepository accountRepository, CustomerRepository customerRepository, EmployeeRepository employeeRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
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
            throw new UsernameNotFoundException("User not found");

        }

        Principal principal = Principal.builder()
                .account(account)
                .userDetails(userDetails)
                .build();

        // This means we found the account instance using the username...
        return new UserPrincipal(principal);
    }
}
