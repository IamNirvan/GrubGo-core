package com.iamnirvan.restaurant.core.models.requests.dish;

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
public class DishCreateRequest {
    @NotBlank(message = "a valid name is required")
    private String name;
    @NotBlank(message = "a description name is required")
    private String description;
    @NotNull(message = "a valid list of dish portions is required")
    private List<DishPortionCreateRequest> dishPortion;
    private MultipartFile image;
}
