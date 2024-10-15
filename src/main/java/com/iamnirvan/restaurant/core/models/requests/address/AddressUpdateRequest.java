package com.iamnirvan.restaurant.core.models.requests.address;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressUpdateRequest {
    @NotNull(message = "a valid address id is required")
    @Min(value = 1, message = "invalid address id")
    private Long id;
    private String city;
    private String province;
    private String street;
    private String buildingNumber;
    private Boolean isMain;
}
