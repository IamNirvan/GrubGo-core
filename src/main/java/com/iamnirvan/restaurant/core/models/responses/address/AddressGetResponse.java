package com.iamnirvan.restaurant.core.models.responses.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddressGetResponse {
    private Long id;
    private String province;
    private String city;
    private String streetName;
    private String buildingNumber;
    private boolean isMain;
}
