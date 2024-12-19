package com.iamnirvan.restaurant.core.models.requests.fuse.rules;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DishDetails {
    private Long id;
    private String name;
    private String description;
    private List<String> ingredients;
}
