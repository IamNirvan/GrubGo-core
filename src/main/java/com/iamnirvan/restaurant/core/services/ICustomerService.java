package com.iamnirvan.restaurant.core.services;

import com.iamnirvan.restaurant.core.models.requests.customer.CustomerCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.customer.CustomerUpdateRequest;
import com.iamnirvan.restaurant.core.models.responses.customer.CustomerCreateResponse;
import com.iamnirvan.restaurant.core.models.responses.customer.CustomerDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.customer.CustomerGetResponse;
import com.iamnirvan.restaurant.core.models.responses.customer.CustomerUpdateResponse;

import java.util.List;

public interface ICustomerService {
    CustomerCreateResponse registerCustomer(CustomerCreateRequest request);

    CustomerUpdateResponse updateCustomer(Long id, CustomerUpdateRequest request);

    CustomerDeleteResponse deleteCustomer(Long id);

    List<CustomerGetResponse> getCustomers(Long id);

}
