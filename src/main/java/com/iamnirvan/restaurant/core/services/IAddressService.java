package com.iamnirvan.restaurant.core.services;

import com.iamnirvan.restaurant.core.models.entities.Address;
import com.iamnirvan.restaurant.core.models.requests.address.AddressCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.address.AddressUpdateRequest;
import com.iamnirvan.restaurant.core.models.responses.address.AddressCreateResponse;
import com.iamnirvan.restaurant.core.models.responses.address.AddressDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.address.AddressUpdateResponse;

import java.util.List;

public interface IAddressService {
    List<AddressCreateResponse> createAddress(List<AddressCreateRequest> requests);
    List<AddressUpdateResponse> updateAddress(List<AddressUpdateRequest> requests);
    List<AddressDeleteResponse> deleteAddress(List<Long> ids);
    List<Address> getAddresses(Long id, Long customerId);
}
