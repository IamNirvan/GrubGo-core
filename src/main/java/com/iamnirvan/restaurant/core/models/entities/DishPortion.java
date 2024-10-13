package com.iamnirvan.restaurant.core.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * Represents associative entity between a dish and a portion size (small, medium, etc.)
 * 1 Dish can have many portion sizes. A portion size can be assigned to many dishes
 * */
@Table(name = "dish_portion")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "dish_portion_sequence", sequenceName = "dish_portion_sequence", allocationSize = 1)
public class DishPortion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dish_portion_sequence")
    private Long id;
    private Double price;
    @ManyToOne(fetch = FetchType.LAZY)
    private Dish dish;
    @ManyToOne(fetch = FetchType.LAZY)
    private Portion portion;
    @OneToMany(mappedBy = "dishPortion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DishPortionCart> dishPortionCarts;
}
