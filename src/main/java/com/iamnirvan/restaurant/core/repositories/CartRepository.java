package com.iamnirvan.restaurant.core.repositories;

import com.iamnirvan.restaurant.core.models.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c WHERE c.customer.id = :customerId AND c.status = 'ACTIVE'")
    Optional<Cart> findCurrentActiveCartByCustomerId(Long customerId);
}