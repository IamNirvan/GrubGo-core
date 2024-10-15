package com.iamnirvan.restaurant.core.models.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Table(name = "employee")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder()
@ToString(callSuper = true)
@SequenceGenerator(name = "employee_sequence", sequenceName = "employee_sequence", allocationSize = 1)
public class Employee extends DateTimeWithoutUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_sequence")
    private Long id;
    private String firstName;
    private String lastName;
    @OneToOne(cascade = CascadeType.REMOVE)
    private Account account;
}
