package com.iamnirvan.restaurant.core.models.responses.address;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressUpdateResponse {
    private String province;
    private String city;
    private String streetName;
    private String buildingNumber;
}
