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
public class DateTimeWithoutUser {
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE", updatable = false)
    public OffsetDateTime created;
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    public OffsetDateTime updated;
}
