package com.iamnirvan.restaurant.core.services;

import com.iamnirvan.restaurant.core.enums.EFoodOrderStatus;
import com.iamnirvan.restaurant.core.models.requests.food_order.FoodOrderCreateRequest;
import com.iamnirvan.restaurant.core.models.responses.food_order.FoodOrderCreateResponse;
import com.iamnirvan.restaurant.core.models.responses.food_order.FoodOrderGetResponse;
import com.iamnirvan.restaurant.core.models.responses.food_order.FoodOrderUpdateStatusResponse;

import java.util.List;

public interface IFoodOrderService {
    List<FoodOrderCreateResponse> placeOrder(List<FoodOrderCreateRequest> requests);
    List<FoodOrderGetResponse> getOrders(Long id, Long customerId);
    FoodOrderUpdateStatusResponse updateOrderStatus(Long id, EFoodOrderStatus status);
}
