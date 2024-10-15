package com.iamnirvan.restaurant.core.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Deprecated
@Table(name = "dish_image")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "dish_image_sequence", sequenceName = "dish_image_sequence", allocationSize = 1)
public class DishImage extends DateTimeWithUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dish_image_sequence")
    private Long id;
    private String name;
    @Column(name = "file_path")
    private String filePath;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id")
    private Dish dish;
}
