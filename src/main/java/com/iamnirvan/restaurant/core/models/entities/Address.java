package com.iamnirvan.restaurant.core.models.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Represents a delivery location of a customer
 * */
@Table(name = "address")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@SequenceGenerator(name = "address_sequence", sequenceName = "address_sequence", allocationSize = 1)
public class Address extends DateTimeWithoutUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_sequence")
    private Long id;
    private String street;
    private String city;
    private String province;
    private String buildingNumber;
    private boolean isMain;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @ToString.Exclude
    private Customer customer;
}
