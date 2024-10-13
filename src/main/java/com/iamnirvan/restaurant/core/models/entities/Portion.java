package com.iamnirvan.restaurant.core.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

/**
 * This represents a specific portion size (small, medium, etc.) of a dish. It is linked with the associative entity
 * */
@Table(name = "portion")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@SequenceGenerator(name = "portion_sequence", sequenceName = "portion_sequence", allocationSize = 1)
public class Portion extends DateTimeWithUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "portion_sequence")
    private Long id;
    @Column(unique = true)
    private String name;
    @OneToMany(mappedBy = "portion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DishPortion> dishPortions;
}
