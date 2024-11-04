package com.iamnirvan.restaurant.core.models.requests.login;

import com.iamnirvan.restaurant.core.models.requests.dish_portion.DishPortionCreateRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "a username is required")
    private String username;
    @NotBlank(message = "a password is required")
    private String password;
}
