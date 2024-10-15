package com.iamnirvan.restaurant.core.repositories;

import com.iamnirvan.restaurant.core.models.entities.DishPortionCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishPortionCartRepository extends JpaRepository<DishPortionCart, Long> {
    DishPortionCart findByCartIdAndDishPortionId(Long cartId, Long dishPortionId);
    @Query("SELECT dpc FROM DishPortionCart dpc WHERE dpc.cart.id = :cartId")
    List<DishPortionCart> findAllByCartId(Long cartId);
}