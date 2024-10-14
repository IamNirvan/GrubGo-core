package com.iamnirvan.restaurant.core.services;

import com.iamnirvan.restaurant.core.models.requests.cart.AddDishIntoCartRequest;
import com.iamnirvan.restaurant.core.models.requests.cart.RemoveDishFromCartRequest;
import com.iamnirvan.restaurant.core.models.responses.cart.AddDishIntoCartResponse;
import com.iamnirvan.restaurant.core.models.responses.cart.GetCartResponse;
import com.iamnirvan.restaurant.core.models.responses.cart.RemoveDishFromCartResponse;

import java.util.List;

public interface ICartService {
    List<AddDishIntoCartResponse> manipulateCartContent(Long id, List<AddDishIntoCartRequest> requests);
    List<RemoveDishFromCartResponse> removeDishPortionFromCart(Long id, List<RemoveDishFromCartRequest> requests);
    GetCartResponse getCartContent(Long id);
}
