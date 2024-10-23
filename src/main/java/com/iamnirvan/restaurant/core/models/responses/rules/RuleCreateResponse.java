package com.iamnirvan.restaurant.core.models.responses.rules;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class RuleCreateResponse {
    private Long id;
    private String factName;
    private String ruleName;
    private String rule;
    private OffsetDateTime created;
    private OffsetDateTime updated;
}
