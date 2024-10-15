package com.iamnirvan.restaurant.core.models.responses.user;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class UserCreateResponse {
    private Long id;
    private String username;
    private Long roleId;
    private String roleName;
    private Boolean active;
}
