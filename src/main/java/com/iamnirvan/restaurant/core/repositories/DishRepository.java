package com.iamnirvan.restaurant.core.repositories;

import com.iamnirvan.restaurant.core.models.entities.Dish;
import com.iamnirvan.restaurant.core.models.responses.dish.DishGetResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    boolean existsByName(String name);
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN TRUE ELSE FALSE END FROM Dish d WHERE d.name = :name AND d.id != :id")
    boolean existsByNameExcludingId(String name, Long id);
}