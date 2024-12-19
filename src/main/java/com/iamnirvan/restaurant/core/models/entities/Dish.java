package com.iamnirvan.restaurant.core.models.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
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
public class Dish extends DateTimeWithoutUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dish_sequence")
    private Long id;
    private String name;
    private String description;
    @OneToMany(mappedBy = "dish", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Review> reviews;
    @Column(name = "image", length = 1000)
    private byte[] image;
    @OneToMany(mappedBy = "dish", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<DishPortion> dishPortions;
    @ElementCollection
    @CollectionTable(name = "dish_ingredients", joinColumns = @JoinColumn(name = "dish_id"))
    @Column(name = "ingredient")
    private List<String> ingredients;
}
