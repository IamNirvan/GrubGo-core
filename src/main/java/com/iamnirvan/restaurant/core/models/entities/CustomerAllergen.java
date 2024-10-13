package com.iamnirvan.restaurant.core.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Represents a customer's allergen. A customer can have many allergens.
 * */
@Table(name = "customer_allergen")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
//@ToString(callSuper = true)
@SequenceGenerator(name = "customer_allergen_sequence", sequenceName = "customer_allergen_sequence", allocationSize = 1)
public class CustomerAllergen extends DateTimeWithoutUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_allergen_sequence")
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
//    @JsonBackReference
//    @ToString.Exclude
    private Customer customer;
}
