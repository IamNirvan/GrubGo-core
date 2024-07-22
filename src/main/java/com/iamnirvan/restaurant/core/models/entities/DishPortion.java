package com.iamnirvan.restaurant.core.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Many-to-many association between dish and portion entity...

@Table(name = "dish_portion")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
}
