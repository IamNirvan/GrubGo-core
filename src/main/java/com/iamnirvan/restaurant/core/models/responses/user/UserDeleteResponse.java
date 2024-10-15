package com.iamnirvan.restaurant.core.models.responses.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDeleteResponse {
    private Long id;
    private String username;
}
