package com.iamnirvan.restaurant.core.models.requests.portion;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PortionUpdateRequest {
    @NotNull(message = "Portion id is required")
    @Min(value = 1, message = "A valid portion id is required")
    private Long id;
    private String name;
}
