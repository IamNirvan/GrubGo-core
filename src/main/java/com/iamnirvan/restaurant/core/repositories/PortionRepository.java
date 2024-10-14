package com.iamnirvan.restaurant.core.repositories;

import com.iamnirvan.restaurant.core.models.entities.Portion;
import com.iamnirvan.restaurant.core.models.responses.portion.PortionGetResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortionRepository extends JpaRepository<Portion, Long> {
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM Portion p WHERE p.name = ?1 AND p.id != ?2")
    boolean existsByName(String name, Long id);
    boolean existsByName(String name);
    @Query("SELECT new com.iamnirvan.restaurant.core.models.responses.portion.PortionGetResponse(p.id, p.name, p.created, p.updated) FROM Portion p")
    List<PortionGetResponse> getAllPortions();
    @Query("SELECT new com.iamnirvan.restaurant.core.models.responses.portion.PortionGetResponse(p.id, p.name, p.created, p.updated) FROM Portion p WHERE p.id = ?1")
    Optional<PortionGetResponse> getPortionById(Long id);
}