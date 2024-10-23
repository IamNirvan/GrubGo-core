package com.iamnirvan.restaurant.core.repositories;

import com.iamnirvan.restaurant.core.models.entities.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {
    boolean existsByRuleName(String name);
    boolean existsByRuleNameAndIdNot(String name, Long id);
    List<Rule> findAllByFact(String fact);
}
