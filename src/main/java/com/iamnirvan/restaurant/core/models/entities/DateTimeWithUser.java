package com.iamnirvan.restaurant.core.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@MappedSuperclass
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DateTimeWithUser {
    private String createdBy;
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE", updatable = false)
    private OffsetDateTime created;
    private String updatedBy;
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE", updatable = false)
    private OffsetDateTime updated;
}
