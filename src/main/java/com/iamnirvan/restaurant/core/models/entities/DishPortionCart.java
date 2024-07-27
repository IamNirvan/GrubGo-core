package com.iamnirvan.restaurant.core.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

// Many-to-many association between dish and portion entity...

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
    @JsonBackReference
    private Cart cart;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private DishPortion dishPortion;

}
