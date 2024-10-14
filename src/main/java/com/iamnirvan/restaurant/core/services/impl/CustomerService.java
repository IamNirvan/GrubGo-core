package com.iamnirvan.restaurant.core.services.impl;

import com.iamnirvan.restaurant.core.enums.EActiveStatus;
import com.iamnirvan.restaurant.core.exceptions.BadRequestException;
import com.iamnirvan.restaurant.core.exceptions.NotFoundException;
import com.iamnirvan.restaurant.core.models.entities.Address;
import com.iamnirvan.restaurant.core.models.entities.Cart;
import com.iamnirvan.restaurant.core.models.entities.Customer;
import com.iamnirvan.restaurant.core.models.entities.CustomerAllergen;
import com.iamnirvan.restaurant.core.models.requests.address.AddressCreateRequestWithoutCustomer;
import com.iamnirvan.restaurant.core.models.requests.customer.CustomerCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.customer.CustomerUpdateRequest;
import com.iamnirvan.restaurant.core.models.responses.customer.CustomerCreateResponse;
import com.iamnirvan.restaurant.core.models.responses.customer.CustomerDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.customer.CustomerGetResponse;
import com.iamnirvan.restaurant.core.models.responses.customer.CustomerUpdateResponse;
import com.iamnirvan.restaurant.core.repositories.CartRepository;
import com.iamnirvan.restaurant.core.repositories.CustomerRepository;
import com.iamnirvan.restaurant.core.services.ICustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomerService implements ICustomerService {
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;

    /**
     * Registers a new customer.
     *
     * @param request the customer creation request containing the details of the new customer
     * @return a response containing the details of the created customer
     * @throws BadRequestException if a customer with the given username already exists
     */
    @Override
    @Transactional
    public CustomerCreateResponse registerCustomer(CustomerCreateRequest request) {
        if (customerRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        Customer customer = Customer.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
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

        log.debug(String.format("Customer created: %s", customer));

        return CustomerCreateResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .created(customer.getCreated())
                .build();
    }

    /**
     * Updates an existing customer.
     *
     * @param id the ID of the customer to update
     * @param request the customer update request containing the new details of the customer
     * @return a response containing the updated details of the customer
     * @throws NotFoundException if the customer with the given ID does not exist
     * @throws BadRequestException if the first name or last name is empty
     */
    @Override
    @Transactional
    public CustomerUpdateResponse updateCustomer(Long id, CustomerUpdateRequest request) {
        Customer customer = customerRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Customer with id %s does not exist", id)));

        if (request.getPassword() != null) {
            customer.setPassword(request.getPassword());
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
        log.debug(String.format("Customer updated: %s", customer));

        return CustomerUpdateResponse.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .password(customer.getPassword())
                .updated(customer.getUpdated())
                .build();
    }

    /**
     * Deletes an existing customer.
     *
     * @param id the ID of the customer to delete
     * @return a response containing the details of the deleted customer
     * @throws NotFoundException if the customer with the given ID does not exist
     */
    @Override
    public CustomerDeleteResponse deleteCustomer(Long id) {
        Customer employee = customerRepository.findById(id).orElseThrow(() ->
                new NotFoundException((String.format("Customer with id %s does not exist", id))));

        customerRepository.delete(employee);
        log.debug(String.format("Customer deleted: %s", employee));

        return CustomerDeleteResponse.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .build();
    }

    /**
     * Retrieves customers.
     *
     * @param id the ID of the customer to retrieve (optional)
     * @return a list of customer responses
     * @throws NotFoundException if the customer with the given ID does not exist
     */
    @Override
    public List<CustomerGetResponse> getCustomers(Long id) {
        if (id != null) {
            CustomerGetResponse customer = customerRepository.findCustomerById(id).orElseThrow(() ->
                    new NotFoundException(String.format("Customer with id %s does not exist", id)));
            return List.of(customer);
        }
        return customerRepository.findAllCustomers();
    }
}
