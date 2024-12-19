package com.iamnirvan.restaurant.core.exceptions;

import org.springframework.http.HttpStatus;

public class ServerException extends ApiException {
    public ServerException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
