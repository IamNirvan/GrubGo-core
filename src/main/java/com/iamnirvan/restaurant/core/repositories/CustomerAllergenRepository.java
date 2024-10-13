package com.iamnirvan.restaurant.core.repositories;

import com.iamnirvan.restaurant.core.models.entities.CustomerAllergen;
import com.iamnirvan.restaurant.core.models.responses.customer_allergen.CustomerAllergenGetResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerAllergenRepository extends JpaRepository<CustomerAllergen, Long> {
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CustomerAllergen c WHERE c.name ILIKE :name AND c.customer.id = :customerId")
    boolean existsByNameAndCustomerId(String name, Long customerId);
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CustomerAllergen c WHERE c.name ILIKE :name AND c.customer.id = :customerId AND c.id != :id")
    boolean existsByNameAndCustomerIdDuringUpdate(Long id, String name, Long customerId);
    @Query("SELECT new com.iamnirvan.restaurant.core.models.responses.customer_allergen.CustomerAllergenGetResponse(c.id, c.name) FROM CustomerAllergen c WHERE c.customer.id = :customerId")
    List<CustomerAllergenGetResponse> findCustomerAllergenByCustomerId(Long customerId);
    @Query("SELECT new com.iamnirvan.restaurant.core.models.responses.customer_allergen.CustomerAllergenGetResponse(c.id, c.name) FROM CustomerAllergen c WHERE c.id = :id")
    Optional<CustomerAllergenGetResponse> findCustomerAllergenById(Long id);

}
