package com.iamnirvan.restaurant.core.models.requests.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AccountCreateRequest {
    @NotBlank(message = "a valid username is required")
    private String username;
    @NotBlank
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
            message = "password must contain at least one lowercase letter, one uppercase letter, " +
                    "one digit, one special character, and must be at least 8 characters long"
    )
    private String password;
    @NotNull(message = "role id is required")
    @Min(value = 1, message = "role id must be greater than 0")
    private Long roleId;
}
