package com.iamnirvan.restaurant.core.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.iamnirvan.restaurant.core.enums.EFoodOrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

/**
 * Links an order instance with a cart that contains all the food portions the customer wanted. This entity
 * is also linked with a customer directly (although the customer is indirectly linked via the cart already...)
 * */
@Table(name = "food_order")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "food_order_sequence", sequenceName = "food_order_sequence", allocationSize = 1)
public class FoodOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "food_order_sequence")
    private Long id;
    private String notes;
    @Enumerated(EnumType.STRING)
    private EFoodOrderStatus status;
    private Double total;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    @JsonBackReference
    private Cart cart;
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE", updatable = false)
    private OffsetDateTime date;
}
