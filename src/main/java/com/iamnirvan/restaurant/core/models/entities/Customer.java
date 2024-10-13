package com.iamnirvan.restaurant.core.models.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

/**
 * Represents a customer in the system. A customer is linked with his/her own cart, reviews, addresses
 * and allergens
 * */
@Table(name = "customer")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder()
@ToString(callSuper = true)
@SequenceGenerator(name = "customer_sequence", sequenceName = "customer_sequence", allocationSize = 1)
public class Customer extends DateTimeWithoutUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_sequence")
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JsonManagedReference
    @ToString.Exclude
    private Set<CustomerAllergen> allergens;
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JsonManagedReference
    @ToString.Exclude
    private Set<Address> addresses;
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Review> reviews;
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JsonManagedReference
    @ToString.Exclude
    private Set<Cart> cart;
}
