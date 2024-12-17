package com.iamnirvan.restaurant.core.services;

import com.iamnirvan.restaurant.core.models.requests.dish.DishCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.dish.DishUpdateRequest;
import com.iamnirvan.restaurant.core.models.responses.dish.DishCreateResponse;
import com.iamnirvan.restaurant.core.models.responses.dish.DishDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.dish.DishGetResponse;
import com.iamnirvan.restaurant.core.models.responses.dish.DishUpdateResponse;
import com.iamnirvan.restaurant.core.models.responses.metrics.DishMetrics;

import java.util.List;

public interface IDishService {
    List<DishCreateResponse> createDish(List<DishCreateRequest> requests);
    List<DishUpdateResponse> updateDish(List<DishUpdateRequest> requests);
    List<DishDeleteResponse> deleteDish(List<Long> ids);
    List<DishGetResponse> getDishes(Long id, boolean includeImg);
    DishMetrics getDishMetrics(Long id);
}
