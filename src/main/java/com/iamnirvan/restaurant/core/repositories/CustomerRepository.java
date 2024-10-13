package com.iamnirvan.restaurant.core.repositories;

import com.iamnirvan.restaurant.core.models.entities.Customer;
import com.iamnirvan.restaurant.core.models.responses.customer.CustomerGetResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByUsername(String username);
    @Query("SELECT new com.iamnirvan.restaurant.core.models.responses.customer.CustomerGetResponse(c.id, c.firstName, c.lastName, c.password, c.updated) FROM Customer c")
    List<CustomerGetResponse> findAllCustomers();
    @Query("SELECT new com.iamnirvan.restaurant.core.models.responses.customer.CustomerGetResponse(c.id, c.firstName, c.lastName, c.password, c.updated) FROM Customer c WHERE c.id = :id")
    Optional<CustomerGetResponse> findCustomerById(Long id);

}
