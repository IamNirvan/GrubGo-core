package com.iamnirvan.restaurant.core.repositories;

import com.iamnirvan.restaurant.core.models.entities.CustomerAllergen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerAllergenRepository extends JpaRepository<CustomerAllergen, Long> {
    List<CustomerAllergen> findCustomerAllergenByCustomerId(Long customerId);
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CustomerAllergen c WHERE c.name ILIKE :name AND c.customer.id = :customerId")
    boolean existsByNameAndCustomerId(String name, Long customerId);
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CustomerAllergen c WHERE c.name ILIKE :name AND c.customer.id = :customerId AND c.id != :id")
    boolean existsByNameAndCustomerIdDuringUpdate(Long id, String name, Long customerId);

}
