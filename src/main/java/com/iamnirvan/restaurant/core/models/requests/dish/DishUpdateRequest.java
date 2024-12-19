package com.iamnirvan.restaurant.core.models.requests.dish;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishUpdateRequest {
    @NotNull(message = "dish id is required")
    @Min(value = 1, message = "dish id must be greater than 0")
    private Long id;
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "dish name must contain only alphanumeric characters and spaces")
    private String name;
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "dish description must contain only alphanumeric characters and spaces")
    private String description;
    private MultipartFile image;
    private List<String> ingredients;
}
