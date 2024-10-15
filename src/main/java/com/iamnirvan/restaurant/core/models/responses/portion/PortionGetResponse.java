package com.iamnirvan.restaurant.core.models.responses.portion;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class PortionGetResponse {
    private Long id;
    private String name;
    private OffsetDateTime created;
    private OffsetDateTime updated;
}
