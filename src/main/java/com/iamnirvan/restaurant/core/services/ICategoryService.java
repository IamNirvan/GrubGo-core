package com.iamnirvan.restaurant.core.services;

import com.iamnirvan.restaurant.core.models.requests.category.CategoryCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.category.CategoryUpdateRequest;
import com.iamnirvan.restaurant.core.models.responses.category.CategoryCreateResponse;
import com.iamnirvan.restaurant.core.models.responses.category.CategoryDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.category.CategoryGetResponse;
import com.iamnirvan.restaurant.core.models.responses.category.CategoryUpdateResponse;
import com.iamnirvan.restaurant.core.models.responses.dish.DishGetResponse;

import java.util.List;

public interface ICategoryService {
    List<CategoryCreateResponse> createCategory(List<CategoryCreateRequest> requests);
    List<CategoryUpdateResponse> updateCategory(List<CategoryUpdateRequest> requests);
    List<CategoryDeleteResponse> deleteCategory(List<Long> ids);
    List<CategoryGetResponse> getCategories(Long id);
}
