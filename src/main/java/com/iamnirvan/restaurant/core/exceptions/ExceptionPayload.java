package com.iamnirvan.restaurant.core.exceptions;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class ExceptionPayload {
    private final String message;
    private final OffsetDateTime dateTime;
}
