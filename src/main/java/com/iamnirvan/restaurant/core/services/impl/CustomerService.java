package com.iamnirvan.restaurant.core.services.impl;

import com.iamnirvan.restaurant.core.enums.EActiveStatus;
import com.iamnirvan.restaurant.core.exceptions.BadRequestException;
import com.iamnirvan.restaurant.core.exceptions.ConflictException;
import com.iamnirvan.restaurant.core.exceptions.NotFoundException;
import com.iamnirvan.restaurant.core.models.entities.*;
import com.iamnirvan.restaurant.core.models.requests.address.AddressCreateRequestWithoutCustomer;
import com.iamnirvan.restaurant.core.models.requests.customer.CustomerCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.customer.CustomerUpdateRequest;
import com.iamnirvan.restaurant.core.models.requests.user.AccountCreateRequest;
import com.iamnirvan.restaurant.core.models.responses.customer.CustomerRegisterResponse;
import com.iamnirvan.restaurant.core.models.responses.customer.CustomerDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.customer.CustomerGetResponse;
import com.iamnirvan.restaurant.core.models.responses.customer.CustomerUpdateResponse;
import com.iamnirvan.restaurant.core.models.responses.user.AccountUpdateResponse;
import com.iamnirvan.restaurant.core.repositories.AccountRepository;
import com.iamnirvan.restaurant.core.repositories.CartRepository;
import com.iamnirvan.restaurant.core.repositories.CustomerRepository;
import com.iamnirvan.restaurant.core.services.ICustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomerService implements ICustomerService {
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final AccountService accountService;

    /**
     * Registers a new customer.
     *
     * @param request the customer creation request containing customer details
     * @return the response containing the created customer's details
     * @throws ConflictException if there is a conflict while creating the user
     * @throws NotFoundException if the role specified in the request is not found
     */
    @Override
    @Transactional
    public CustomerRegisterResponse registerCustomer(@NotNull CustomerCreateRequest request) {
        // First create the customer's account
        Account account = null;
        try {
            AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
            accountCreateRequest.setPassword(request.getPassword());
            accountCreateRequest.setUsername(request.getUsername());
            accountCreateRequest.setRoleId(request.getRoleId());
            account = accountService.createAccount(accountCreateRequest);
        } catch (ConflictException | NotFoundException ex) {
            log.error("Error creating user", ex);
            throw ex;
        }

        // Then create the customer instance
        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .account(account)
                .created(OffsetDateTime.now())
                .build();

        customerRepository.save(customer);

        // Handle the allergens
       Set<CustomerAllergen> allergens = null;
       if (request.getAllergens() != null && !request.getAllergens().isEmpty()) {
           allergens = new HashSet<>(request.getAllergens().size());

           for (String allergen : request.getAllergens()) {
               allergens.add(
                       CustomerAllergen.builder()
                               .name(allergen)
                               .customer(customer)
                               .created(OffsetDateTime.now())
                               .build()
               );
           }
           log.debug("handled customer allergens");
       }

       // Handle the addresses
       Set<Address> addresses = null;
       if (request.getAddresses() != null && !request.getAddresses().isEmpty()) {
           addresses = new HashSet<>(request.getAddresses().size());
           boolean alreadyHaveMainAddress = false;

           for (AddressCreateRequestWithoutCustomer address : request.getAddresses()) {
               if (!alreadyHaveMainAddress && address.isMain()) {
                   alreadyHaveMainAddress = true;
               } else if (alreadyHaveMainAddress && address.isMain()) {
                   throw new BadRequestException("Only one address can be marked as main");
               }

               addresses.add(
                       Address.builder()
                               .street(address.getStreet())
                               .city(address.getCity())
                               .province(address.getProvince())
                               .buildingNumber(address.getBuildingNumber())
                               .isMain(address.isMain())
                               .customer(customer)
                               .created(OffsetDateTime.now())
                               .build()
               );
           }
           log.debug("handled customer addresses");
       }

       customer.setAllergens(allergens);
       customer.setAddresses(addresses);
       customerRepository.save(customer);

        // Create a default cart for the customer
        Cart cart = Cart.builder()
                .customer(customer)
                .status(EActiveStatus.ACTIVE)
                .build();
        cartRepository.save(cart);

        log.debug("Customer created: {}", customer);

        return Parser.toCustomerRegisterResponse(customer);
    }

    /**
     * Updates an existing customer.
     *
     * @param id the ID of the customer to update
     * @param request the customer update request containing updated details
     * @return the response containing the updated customer's details
     * @throws NotFoundException if the customer with the specified ID does not exist
     * @throws BadRequestException if the first name or last name is empty
     */
    @Override
    @Transactional
    public CustomerUpdateResponse updateCustomer(Long id, CustomerUpdateRequest request) {
        Customer customer = customerRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Customer with id %s does not exist", id)));

        AccountUpdateResponse accountUpdateResponse = null;
        try {
            if (request.getAccountInfo() != null) {
                accountUpdateResponse = accountService.updateAccount(customer.getAccount().getId(), request.getAccountInfo());
            }
        } catch (Exception ex) {
            log.error("Error updating user", ex);
            throw ex;
        }

        if (request.getFirstName() != null) {
            if (request.getFirstName().isEmpty()) {
                throw new BadRequestException("First name cannot be empty");
            }
            customer.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            if(request.getLastName().isEmpty()) {
                throw new BadRequestException("Last name cannot be empty");
            }
            customer.setLastName(request.getLastName());
        }

        customer.setUpdated(OffsetDateTime.now());
        customerRepository.save(customer);
        log.debug("Customer updated: {}", customer);

        return Parser.toCustomerUpdateResponse(customer);
    }

    /**
     * Deletes an existing customer.
     *
     * @param id the ID of the customer to delete
     * @return the response containing the deleted customer's details
     * @throws NotFoundException if the customer with the specified ID does not exist
     */
    @Override
    public CustomerDeleteResponse deleteCustomer(Long id) {
        Customer employee = customerRepository.findById(id).orElseThrow(() ->
                new NotFoundException((String.format("Customer with id %s does not exist", id))));

        customerRepository.delete(employee);
        log.debug("Customer deleted: {}", employee);

        return CustomerDeleteResponse.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .build();
    }

    /**
     * Retrieves a list of customers or a specific customer by ID.
     *
     * @param id the ID of the customer to retrieve (optional)
     * @return a list of customer responses
     * @throws NotFoundException if the customer with the specified ID does not exist
     */
    @Override
    public List<CustomerGetResponse> getCustomers(Long id) {
        if (id != null) {
            Customer customer = customerRepository.findById(id).orElseThrow(() ->
                    new NotFoundException(String.format("Customer with id %s does not exist", id)));
            return List.of(Parser.toCustomerGetResponse(customer));
        }
        return customerRepository.findAll().stream().map(Parser::toCustomerGetResponse).collect(Collectors.toList());
    }


    public static class Parser {
        public static CustomerUpdateResponse toCustomerUpdateResponse(Customer customer) {
            final Account account = customer.getAccount();

            return CustomerUpdateResponse.builder()
                    .id(customer.getId())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .accountInfo(AccountService.Parser.toAccountUpdateResponse(account))
                    .updated(customer.getUpdated())
                    .build();
        }

        public static CustomerGetResponse toCustomerGetResponse(Customer customer) {
            final Account account = customer.getAccount();

            return CustomerGetResponse.builder()
                    .id(customer.getId())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .account(AccountService.Parser.toAccountGetResponse(account))
                    .created(customer.getCreated())
                    .updated(customer.getUpdated())
                    .build();
        }

        public static CustomerRegisterResponse toCustomerRegisterResponse(Customer customer) {
            final Account account = customer.getAccount();

            return CustomerRegisterResponse.builder()
                    .id(customer.getId())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .accountInfo(AccountService.Parser.toAccountGetResponse(account))
                    .created(customer.getCreated())
                    .build();
        }
    }
}
