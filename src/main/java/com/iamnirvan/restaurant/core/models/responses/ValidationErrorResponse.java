package com.iamnirvan.restaurant.core.models.responses;

import lombok.Data;

@Data
public class ValidationErrorResponse {
    private String field;
    private String errorMessage;

    public ValidationErrorResponse(String field, String errorMessage) {
        this.field = field;
        this.errorMessage = errorMessage;
    }
}