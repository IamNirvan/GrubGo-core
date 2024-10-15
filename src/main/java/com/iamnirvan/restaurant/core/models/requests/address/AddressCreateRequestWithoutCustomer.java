package com.iamnirvan.restaurant.core.models.requests.address;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressCreateRequestWithoutCustomer {
    @NotBlank(message = "a valid city is required")
    private String city;
    @NotBlank(message = "a valid province name is required")
    private String province;
    @NotBlank(message = "a valid street name is required")
    private String street;
    @NotBlank(message = "a valid building number is required")
    private String buildingNumber;
    private boolean isMain;
}
