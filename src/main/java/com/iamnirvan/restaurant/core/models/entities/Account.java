package com.iamnirvan.restaurant.core.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "account")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "account_sequence", sequenceName = "account_sequence", allocationSize = 1)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    private Long id;
    private String username;
    private String password;
    private Boolean active;
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    private Roles roles;
}
