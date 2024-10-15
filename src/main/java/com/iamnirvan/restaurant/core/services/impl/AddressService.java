package com.iamnirvan.restaurant.core.services.impl;

import com.iamnirvan.restaurant.core.exceptions.BadRequestException;
import com.iamnirvan.restaurant.core.exceptions.NotFoundException;
import com.iamnirvan.restaurant.core.models.entities.Address;
import com.iamnirvan.restaurant.core.models.entities.Customer;
import com.iamnirvan.restaurant.core.models.requests.address.AddressCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.address.AddressUpdateRequest;
import com.iamnirvan.restaurant.core.models.responses.address.AddressCreateResponse;
import com.iamnirvan.restaurant.core.models.responses.address.AddressDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.address.AddressGetResponse;
import com.iamnirvan.restaurant.core.models.responses.address.AddressUpdateResponse;
import com.iamnirvan.restaurant.core.repositories.AddressRepository;
import com.iamnirvan.restaurant.core.repositories.CustomerRepository;
import com.iamnirvan.restaurant.core.services.IAddressService;
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
public class AddressService implements IAddressService {
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    @Override
    public List<AddressCreateResponse> createAddress(List<AddressCreateRequest> requests) {
        final List<AddressCreateResponse> result = new ArrayList<>();

        for (AddressCreateRequest request : requests) {
            Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(() ->
                    new NotFoundException(String.format("Customer with id %s does not exist", request.getCustomerId())));

            Address mainAddress = addressRepository.getMainAddress(customer.getId());
            if (mainAddress != null && request.isMain()) {
                throw new BadRequestException("Main address already exists for this customer");
            }

            Address address = Address.builder()
                    .city(request.getCity())
                    .province(request.getProvince())
                    .street(request.getStreet())
                    .buildingNumber(request.getBuildingNumber())
                    .isMain(mainAddress == null)
                    .customer(customer)
                    .created(OffsetDateTime.now())
                    .build();
            addressRepository.save(address);
            log.debug(String.format("Address created: %s", address));

            result.add(AddressCreateResponse.builder()
                    .id(address.getId())
                    .province(address.getProvince())
                    .city(address.getCity())
                    .streetName(address.getStreet())
                    .buildingNumber(address.getBuildingNumber())
                    .build());
        }
        return result;
    }

    @Transactional
    @Override
    public List<AddressUpdateResponse> updateAddress(List<AddressUpdateRequest> requests) {
        List<AddressUpdateResponse> result = new ArrayList<>();

        for (AddressUpdateRequest request : requests) {
            Address address = addressRepository.findById(request.getId()).orElseThrow(() ->
                    new NotFoundException(String.format("Address with id %s does not exist", request.getId())));

            if (request.getProvince() != null) {
                if (request.getProvince().isEmpty()) {
                    throw new BadRequestException("Province cannot be empty");
                }
                address.setProvince(request.getProvince());
            }

            if (request.getCity() != null) {
                if (request.getCity().isEmpty()) {
                    throw new BadRequestException("City cannot be empty");
                }
                address.setCity(request.getCity());
            }

            if (request.getStreet() != null) {
                if (request.getStreet().isEmpty()) {
                    throw new BadRequestException("Street name cannot be empty");
                }
                address.setStreet(request.getStreet());
            }

            if (request.getBuildingNumber() != null) {
                if (request.getBuildingNumber().isEmpty()) {
                    throw new BadRequestException("Building number cannot be empty");
                }
                address.setBuildingNumber(request.getBuildingNumber());
            }

            if (request.getIsMain() != null) {
                Long customerId = address.getCustomer().getId();
                Address mainAddress = addressRepository.getMainAddress(customerId);
                mainAddress.setMain(false);
                addressRepository.save(mainAddress);
                address.setMain(true);
            }

            address.setUpdated(OffsetDateTime.now());
            addressRepository.save(address);
            log.debug(String.format("Address updated: %s", address));

            result.add(AddressUpdateResponse.builder()
                    .province(address.getProvince())
                    .city(address.getCity())
                    .streetName(address.getStreet())
                    .buildingNumber(address.getBuildingNumber())
                    .build());
        }
        return result;
    }

    @Transactional
    @Override
    public List<AddressDeleteResponse> deleteAddress(List<Long> ids) {
        List<AddressDeleteResponse> result = new ArrayList<>();

        for (Long id : ids) {
            Address address = addressRepository.findById(id).orElseThrow(()
                    -> new NotFoundException(String.format("Address with id %s does not exist", id)));

            addressRepository.delete(address);
            log.debug(String.format("Address deleted: %s", address));

            result.add(AddressDeleteResponse.builder()
                    .id(address.getId())
                    .province(address.getProvince())
                    .city(address.getCity())
                    .streetName(address.getStreet())
                    .buildingNumber(address.getBuildingNumber())
                    .build());
        }
        return result;
    }

    @Override
    public List<AddressGetResponse> getAddresses(Long id, Long customerId) {
        if (id != null) {
            AddressGetResponse address = addressRepository.findAddressById(id).orElseThrow(() ->
                    new NotFoundException((String.format("Address with id %s does not exist", id))));
            return List.of(address);
        }

        if (customerId != null) {
            if (customerRepository.findById(customerId).isEmpty()) {
                throw new NotFoundException(String.format("Customer with id %s does not exist", customerId));
            }
            return addressRepository.findAllAddressesByCustomerId(customerId);
        }
        return null;
    }
}
