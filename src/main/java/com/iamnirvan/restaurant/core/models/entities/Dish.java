package com.iamnirvan.restaurant.core.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

/**
 * This represents a specific dish (BBQ chicken pizza). It is linked with its image and reviews. Also,
 * it is linked with the associative entity DishPortions so that this dish can have many portion sizes...
 * */
@Table(name = "dish")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@SequenceGenerator(name = "dish_sequence", sequenceName = "dish_sequence", allocationSize = 1)
public class Dish extends DateTimeWithUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dish_sequence")
    private Long id;
    private String name;
    private String description;
    @OneToMany(mappedBy = "dish", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Review> reviews;
    @Column(name = "image", length = 1000)
    private byte[] image;
    @OneToMany(mappedBy = "dish", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DishPortion> dishPortions;
}
