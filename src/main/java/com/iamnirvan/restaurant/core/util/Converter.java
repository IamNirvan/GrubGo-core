package com.iamnirvan.restaurant.core.util;

import com.iamnirvan.restaurant.core.models.entities.Dish;
import com.iamnirvan.restaurant.core.models.responses.dish.DishSimplifiedGetResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class Converter {
    public List<DishSimplifiedGetResponse> toDishSimplifiedGetResponse(Set<Dish> dishes) {
        final List<DishSimplifiedGetResponse> result = new ArrayList<>();

        for (Dish dish : dishes) {
            result.add(DishSimplifiedGetResponse.builder()
                    .id(dish.getId())
                    .name(dish.getName())
                    .description(dish.getDescription())
//                    .images(dish.getImages())
                    .build());
        }
        return result;
    }
}
