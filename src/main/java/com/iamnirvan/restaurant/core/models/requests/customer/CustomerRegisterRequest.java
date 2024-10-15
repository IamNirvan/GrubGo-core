package com.iamnirvan.restaurant.core.models.requests.customer;

import com.iamnirvan.restaurant.core.models.requests.address.AddressCreateRequestWithoutCustomer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class CustomerRegisterRequest {
    @NotBlank(message = "a valid username is required")
    private String username;
    @NotBlank
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
            message = "password must contain at least one lowercase letter, one uppercase letter, " +
                    "one digit, one special character, and must be at least 8 characters long"
    )
    private String password;
    @NotBlank(message = "first name is required")
    private String firstName;
    @NotBlank(message = "last name is required")
    private String lastName;
    private List<String> allergens;
    private List<AddressCreateRequestWithoutCustomer> addresses;
}
