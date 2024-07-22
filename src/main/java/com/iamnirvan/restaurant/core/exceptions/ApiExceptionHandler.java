package com.iamnirvan.restaurant.core.exceptions;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.OffsetDateTime;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = ApiException.class)
    protected ResponseEntity<ExceptionPayload> handleException(ApiException ex) {
        final ExceptionPayload payload = ExceptionPayload.builder()
                .message(ex.getMessage())
                .dateTime(OffsetDateTime.now())
                .build();

        return ResponseEntity.status(ex.getStatus()).contentType(MediaType.APPLICATION_JSON).body(payload);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionPayload> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = "an error occurred";
        if (ex.getBindingResult().getFieldError() != null) {
            message = ex.getBindingResult().getFieldError().getDefaultMessage();
        }

        final ExceptionPayload payload = ExceptionPayload.builder()
                .message(message)
                .dateTime(OffsetDateTime.now())
                .build();

        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(payload);
    }
}
