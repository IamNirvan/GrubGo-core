package com.iamnirvan.restaurant.core.models.requests.address;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressCreateRequest {
    @NotBlank(message = "a valid city is required")
    private String city;
    @NotBlank(message = "a valid province name is required")
    private String province;
    @NotBlank(message = "a valid street name is required")
    private String street;
    @NotBlank(message = "a valid building number is required")
    private String buildingNumber;
    @NotNull(message = "a valid customer id is required")
    @Min(value = 1, message = "invalid customer id")
    private Long customerId;
    private boolean isMain;
}
