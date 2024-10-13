package com.iamnirvan.restaurant.core.models.entities;

import com.iamnirvan.restaurant.core.enums.EActiveStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * Represents a cart. A cart has a customer. Additionally, a cart can have multiple dish portions (small pizza, etc.)
 * A cart contains all the desired food portions and is linked with an order. A cart can only have 1 order.
 * */
@Table(name = "cart")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "cart_sequence", sequenceName = "cart_sequence", allocationSize = 1)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_sequence")
    private Long id;
    @Enumerated(EnumType.STRING)
    private EActiveStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;
    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DishPortionCart> dishPortionCarts;
    @OneToOne(mappedBy = "cart", fetch = FetchType.EAGER)
    private FoodOrder foodOrder;
}
