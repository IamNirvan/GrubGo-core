package com.iamnirvan.restaurant.core.repositories;

import com.iamnirvan.restaurant.core.models.entities.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {
    @Query("SELECT fo FROM FoodOrder fo WHERE fo.cart.customer.id = ?1 ORDER BY fo.date ASC")
    List<FoodOrder> findAllByCustomerIdOrderByDateAsc(Long customerId);
    @Query("SELECT fo FROM FoodOrder fo ORDER BY fo.date ASC")
    List<FoodOrder> findAllOrderByDateAsc();
}
