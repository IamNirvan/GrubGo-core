package com.iamnirvan.restaurant.core.models.responses.rules.evaluation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payload {
    private String type;
    private String status;
    private String text;
}
