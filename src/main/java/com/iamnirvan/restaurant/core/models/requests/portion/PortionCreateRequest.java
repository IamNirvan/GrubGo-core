package com.iamnirvan.restaurant.core.models.requests.portion;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PortionCreateRequest {
    @NotBlank
    private String name;
}
