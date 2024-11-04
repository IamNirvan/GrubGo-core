package com.iamnirvan.restaurant.core.models.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Table(name = "rules")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@SequenceGenerator(name = "rules_sequence", sequenceName = "rules_sequence", allocationSize = 1)
public class Rule extends DateTimeWithoutUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rules_sequence")
    private Long id;
    @Column(unique = true)
    private String ruleName;
    // Each rule belongs to a certain fact... This stores the name of the fact
    private String fact;
    @Column(columnDefinition = "TEXT")
    private String rule;
}
