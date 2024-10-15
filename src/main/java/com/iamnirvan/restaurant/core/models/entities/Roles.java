package com.iamnirvan.restaurant.core.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "roles")
@Entity
@Data
@SequenceGenerator(name = "roles_sequence", sequenceName = "roles_sequence", allocationSize = 1)
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_sequence")
    private Long id;
    @Column(unique = true)
    private String name;
}
