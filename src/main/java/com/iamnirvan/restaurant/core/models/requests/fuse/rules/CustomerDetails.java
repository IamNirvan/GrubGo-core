package com.iamnirvan.restaurant.core.models.requests.fuse.rules;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDetails {
    private Long id;
    private String firstName;
    private String lastName;
    private List<String> allergens;
}
