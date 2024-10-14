package com.iamnirvan.restaurant.core.models.requests.dish;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishUpdateRequestList {
    private List<DishUpdateRequest> requests;
}
