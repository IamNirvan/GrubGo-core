package com.iamnirvan.restaurant.core.models.responses.portion;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class PortionCreateResponse {
    private Long id;
    private String name;
    private String createdBy;
    private OffsetDateTime created;
    private String updatedBy;
    private OffsetDateTime updated;
}
