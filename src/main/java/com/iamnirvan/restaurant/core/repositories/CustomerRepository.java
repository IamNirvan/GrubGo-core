package com.iamnirvan.restaurant.core.repositories;

import com.iamnirvan.restaurant.core.models.entities.Customer;
import com.iamnirvan.restaurant.core.models.responses.customer.CustomerTokenDataGetResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("Select new com.iamnirvan.restaurant.core.models.responses.customer.CustomerTokenDataGetResponse(c.id, c.firstName, c.lastName) FROM Customer c WHERE c.account.id = :accountId")
    CustomerTokenDataGetResponse findCustomerByAccountId(Long accountId);
}
