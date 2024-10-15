package com.iamnirvan.restaurant.core.models.responses.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountGetResponse {
    private Long id;
    private String username;
    private Long roleId;
    private String roleName;
    private Boolean active;
}
