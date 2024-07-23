package com.iamnirvan.restaurant.core.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

// Many-to-many association between dish and portion entity...

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
    @JsonBackReference
    private Dish dish;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Portion portion;
}
