package com.iamnirvan.restaurant.core.repositories;

import com.iamnirvan.restaurant.core.models.entities.DishPortionCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishPortionCartRepository extends JpaRepository<DishPortionCart, Long> {
}