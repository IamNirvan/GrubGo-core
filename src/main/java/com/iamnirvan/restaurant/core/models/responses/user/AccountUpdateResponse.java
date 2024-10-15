package com.iamnirvan.restaurant.core.models.responses.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountUpdateResponse {
    private Long id;
    private String username;
    private Boolean active;
}
