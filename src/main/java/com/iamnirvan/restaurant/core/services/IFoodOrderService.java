package com.iamnirvan.restaurant.core.services;

import com.iamnirvan.restaurant.core.models.requests.food_order.FoodOrderCreateRequest;
import com.iamnirvan.restaurant.core.models.responses.food_order.FoodOrderCreateResponse;

import java.util.List;

public interface IFoodOrderService {
    List<FoodOrderCreateResponse> placeOrder(List<FoodOrderCreateRequest> requests);
}
