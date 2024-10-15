package com.iamnirvan.restaurant.core.repositories;

import com.iamnirvan.restaurant.core.models.entities.DishPortion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishPortionRepository extends JpaRepository<DishPortion, Long> {
    boolean existsByPortionId(Long portionId);
    boolean existsByDishIdAndPortionId(Long dishId, Long portionId);
    boolean existsByDishNameAndPortionId(String name, Long portionId);
}