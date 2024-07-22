package com.iamnirvan.restaurant.core.services;

import com.iamnirvan.restaurant.core.models.entities.CustomerAllergen;
import com.iamnirvan.restaurant.core.models.requests.customer_allergen.CustomerAllergenCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.customer_allergen.CustomerAllergenUpdateRequest;
import com.iamnirvan.restaurant.core.models.responses.customer_allergen.CustomerAllergenCreateResponse;
import com.iamnirvan.restaurant.core.models.responses.customer_allergen.CustomerAllergenDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.customer_allergen.CustomerAllergenUpdateResponse;

import java.util.List;

public interface ICustomerAllergenService {
    List<CustomerAllergenCreateResponse> createAllergen(List<CustomerAllergenCreateRequest> requests);
    List<CustomerAllergenUpdateResponse> updateAllergen(List<CustomerAllergenUpdateRequest> requests);
    List<CustomerAllergenDeleteResponse> deleteAllergen(List<Long> ids);
    List<CustomerAllergen> getAllergens(Long id, Long customerId);
}
