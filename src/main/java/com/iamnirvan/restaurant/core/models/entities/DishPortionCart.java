package com.iamnirvan.restaurant.core.models.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents the associative entity between the dish portion (small burger) and a cart. It also contains the
 * desires quantity of the dish portion.
 * */
@Table(name = "dish_portion_cart")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "dish_portion_cart_sequence", sequenceName = "dish_portion_cart_sequence", allocationSize = 1)
public class DishPortionCart {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dish_portion_cart_sequence")
    private Long id;
    private Integer quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;
    @ManyToOne(fetch = FetchType.LAZY)
    private DishPortion dishPortion;
}
