package com.iamnirvan.restaurant.core.models.requests.fuse.rules;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvaluateRulesRequest {
    private DishDetails dish;
    private CustomerDetails customer;
    private ArrayList<Object> responses;
}
