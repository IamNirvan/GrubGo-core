package com.iamnirvan.restaurant.core.services.impl;

import com.iamnirvan.restaurant.core.exceptions.BadRequestException;
import com.iamnirvan.restaurant.core.exceptions.NotFoundException;
import com.iamnirvan.restaurant.core.models.entities.Customer;
import com.iamnirvan.restaurant.core.models.entities.CustomerAllergen;
import com.iamnirvan.restaurant.core.models.requests.customer_allergen.CustomerAllergenCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.customer_allergen.CustomerAllergenUpdateRequest;
import com.iamnirvan.restaurant.core.models.responses.customer_allergen.CustomerAllergenCreateResponse;
import com.iamnirvan.restaurant.core.models.responses.customer_allergen.CustomerAllergenDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.customer_allergen.CustomerAllergenUpdateResponse;
import com.iamnirvan.restaurant.core.repositories.CustomerAllergenRepository;
import com.iamnirvan.restaurant.core.repositories.CustomerRepository;
import com.iamnirvan.restaurant.core.services.ICustomerAllergenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomerAllergenService implements ICustomerAllergenService {
    private final CustomerAllergenRepository allergenRepository;
    private final CustomerRepository customerRepository;

    @Override
    public List<CustomerAllergenCreateResponse> createAllergen(List<CustomerAllergenCreateRequest> requests) {
        List<CustomerAllergenCreateResponse> result = new ArrayList<>();

        for (CustomerAllergenCreateRequest request : requests) {
            Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(
                    () -> new NotFoundException(String.format("Customer with id %s does not exist", request.getCustomerId()))
            );

            if (allergenRepository.existsByNameAndCustomerId(request.getName(), request.getCustomerId())) {
                throw new BadRequestException(String.format("Allergen with name %s already exists", request.getName()));
            }

            CustomerAllergen allergen = CustomerAllergen.builder()
                    .name(request.getName())
                    .customer(customer)
                    .created(OffsetDateTime.now())
                    .build();

            allergenRepository.save(allergen);
            log.debug(String.format("Allergen created %s", allergen));

            result.add(CustomerAllergenCreateResponse.builder()
                    .name(allergen.getName())
                    .build());
        }
        return result;
    }

    @Transactional
    @Override
    public List<CustomerAllergenUpdateResponse> updateAllergen(List<CustomerAllergenUpdateRequest> requests) {
        final List<CustomerAllergenUpdateResponse> result = new ArrayList<>();

        for (CustomerAllergenUpdateRequest request : requests) {
            CustomerAllergen allergen = allergenRepository.findById(request.getId()).orElseThrow(
                    () -> new NotFoundException(String.format("Allergen with id %s does not exist", request.getId()))
            );

            if (allergenRepository.existsByNameAndCustomerIdDuringUpdate(request.getId(), request.getName(), allergen.getCustomer().getId())) {
                throw new BadRequestException(String.format("Allergen with name %s already exists", request.getName()));
            }

            allergen.setName(request.getName());
            allergen.setUpdated(OffsetDateTime.now());
            allergenRepository.save(allergen);
            log.debug(String.format("Allergen updated %s", allergen));

            result.add(CustomerAllergenUpdateResponse.builder()
                    .name(allergen.getName())
                    .build());
        }
        return result;
    }

    @Transactional
    @Override
    public List<CustomerAllergenDeleteResponse> deleteAllergen(List<Long> ids) {
        final List<CustomerAllergenDeleteResponse> result = new ArrayList<>();

        for (Long id: ids) {
            CustomerAllergen allergen = allergenRepository.findById(id).orElseThrow(
                    () -> new NotFoundException(String.format("Allergen with id %s does not exist", id))
            );
            allergenRepository.delete(allergen);
            log.debug(String.format("Allergen deleted %s", allergen));

            result.add(CustomerAllergenDeleteResponse.builder()
                    .id(allergen.getId())
                    .name(allergen.getName())
                    .build());
        }
        return result;
    }

    @Override
    public List<CustomerAllergen> getAllergens(Long id, Long customerId) {
        if (id != null) {
            CustomerAllergen address = allergenRepository.findById(id).orElseThrow(() ->
                    new NotFoundException((String.format("Allergen with id %s does not exist", id))));
            return List.of(address);
        } else if (customerId != null) {
            if (customerRepository.findById(customerId).isEmpty()) {
                throw new NotFoundException(String.format("Customer with id %s does not exist", customerId));
            }
            return allergenRepository.findCustomerAllergenByCustomerId(customerId);
        }
        return allergenRepository.findAll();
    }
}
