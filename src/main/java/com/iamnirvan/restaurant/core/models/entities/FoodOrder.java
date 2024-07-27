package com.iamnirvan.restaurant.core.models.entities;

import com.iamnirvan.restaurant.core.enums.EStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Table(name = "food_order")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "food_order_sequence", sequenceName = "food_order_sequence", allocationSize = 1)
public class FoodOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "food_order_sequence")
    private Long id;
    private String notes;
    private Integer quantity;
    private EStatus status;
    private Double total;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
//    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
//    @JoinTable(
//            name = "order_portion",
//            joinColumns = @JoinColumn(name = "order_id"),
//            inverseJoinColumns = @JoinColumn(name = "portion_id")
//    )
//    private Set<Portion> portions;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "food_orders_dish_portions",
            joinColumns = @JoinColumn(name = "food_order_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_portion_id")
    )
    private Set<DishPortion> dishPortions;
//    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
//    @JoinTable(
//            name = "order_dish",
//            joinColumns = @JoinColumn(name = "order_id"),
//            inverseJoinColumns = @JoinColumn(name = "dish_id")
//    )
//    private Set<Dish> dishes;
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE", updatable = false)
    private Instant date;
}
