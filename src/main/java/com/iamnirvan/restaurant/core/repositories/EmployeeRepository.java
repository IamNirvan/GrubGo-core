package com.iamnirvan.restaurant.core.repositories;

import com.iamnirvan.restaurant.core.models.entities.Employee;
import com.iamnirvan.restaurant.core.models.responses.employee.EmployeeTokenDataGetResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
//    boolean existsByUsername(String username);
    @Query("SELECT new com.iamnirvan.restaurant.core.models.responses.employee.EmployeeTokenDataGetResponse(e.id, e.firstName, e.lastName) FROM Employee e WHERE e.account.id = :accountId")
    EmployeeTokenDataGetResponse findEmployeeByAccountId(Long accountId);
}
