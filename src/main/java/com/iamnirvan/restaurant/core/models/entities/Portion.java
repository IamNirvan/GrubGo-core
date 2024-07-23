package com.iamnirvan.restaurant.core.models.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Table(name = "portion")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@SequenceGenerator(name = "portion_sequence", sequenceName = "portion_sequence", allocationSize = 1)
public class Portion extends DateTimeWithUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "portion_sequence")
    private Long id;
    private String name;
    @OneToMany(mappedBy = "portion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<DishPortion> dishPortions;
    @ManyToMany(mappedBy = "portions", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<FoodOrder> foodOrders;
}
