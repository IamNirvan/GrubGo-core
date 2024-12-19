package com.iamnirvan.restaurant.core.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.iamnirvan.restaurant.core.exceptions.ConflictException;
import com.iamnirvan.restaurant.core.exceptions.NotFoundException;
import com.iamnirvan.restaurant.core.exceptions.ServerException;
import com.iamnirvan.restaurant.core.models.entities.Customer;
import com.iamnirvan.restaurant.core.models.entities.CustomerAllergen;
import com.iamnirvan.restaurant.core.models.entities.Dish;
import com.iamnirvan.restaurant.core.models.entities.Rule;
import com.iamnirvan.restaurant.core.models.requests.rules.RuleCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.rules.RuleUpdateRequest;
import com.iamnirvan.restaurant.core.models.requests.rules.evaluation.EvaluateRulesRequest;
import com.iamnirvan.restaurant.core.models.responses.rules.RuleCreateResponse;
import com.iamnirvan.restaurant.core.models.responses.rules.RuleDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.rules.RuleGetResponse;
import com.iamnirvan.restaurant.core.models.responses.rules.RuleUpdateResponse;
import com.iamnirvan.restaurant.core.models.responses.rules.evaluation.EvaluateRuleResponse;
import com.iamnirvan.restaurant.core.repositories.CustomerRepository;
import com.iamnirvan.restaurant.core.repositories.DishRepository;
import com.iamnirvan.restaurant.core.repositories.RuleRepository;
import com.iamnirvan.restaurant.core.services.IRuleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class RuleService implements IRuleService {
    @Value("${rule.engine.url}")
    private String ruleEngineBaseUrl;
    private final RuleRepository ruleRepository;
    private final DishRepository dishRepository;
    private final CustomerRepository customerRepository;
    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * Creates new rules based on the provided list of rule creation requests.
     *
     * @param requests the list of rule creation requests
     * @return a list of responses containing the details of the created rules
     * @throws ConflictException if a rule with the same name already exists
     */
    @Override
    @Transactional
    public List<RuleCreateResponse> createRule(List<RuleCreateRequest> requests) {
        final List<RuleCreateResponse> response = new ArrayList<>();

        for (RuleCreateRequest request : requests) {
            if (ruleRepository.existsByRuleName(request.getRuleName())) {
                throw new ConflictException(String.format("Rule with name %s already exists", request.getRuleName()));
            }

            final Rule rule = Rule.builder()
                    .fact(request.getFact())
                    .ruleName(request.getRuleName())
                    .rule(request.getRule())
                    .created(OffsetDateTime.now())
                    .build();
            ruleRepository.save(rule);

            log.debug("Rule created: {}", rule);
            response.add(Parser.toRuleCreateResponse(rule));
        }
        return response;
    }

    /**
     * Updates existing rules based on the provided list of rule update requests.
     *
     * @param requests the list of rule update requests
     * @return a list of responses containing the details of the updated rules
     * @throws NotFoundException if a rule with the specified ID is not found
     * @throws ConflictException if a rule with the same name already exists
     */
    @Override
    @Transactional
    public List<RuleUpdateResponse> updateRule(List<RuleUpdateRequest> requests) {
        final List<RuleUpdateResponse> response = new ArrayList<>();

        for (RuleUpdateRequest request : requests) {
            final Rule rule = ruleRepository.findById(request.getId()).orElseThrow(
                    () -> new NotFoundException(String.format("Rule with id %s not found", request.getId())));

            if (request.getRule() != null) {
                rule.setRule(request.getRule());
            }

            if (request.getRuleName() != null) {
                if (ruleRepository.existsByRuleNameAndIdNot(request.getRuleName(), request.getId())) {
                    throw new ConflictException(String.format("Rule with name %s already exists", request.getRuleName()));
                }
                rule.setRuleName(request.getRuleName());
            }
            rule.setUpdated(OffsetDateTime.now());
            ruleRepository.save(rule);

            log.debug("Rule updated: {}", rule);
            response.add(Parser.toRuleUpdateResponse(rule));
        }
        return response;
    }

    /**
     * Retrieves rules based on the provided rule ID or fact name.
     *
     * @param id the ID of the rule to retrieve (optional)
     * @param factName the fact name to filter rules by (optional)
     * @return a list of responses containing the details of the retrieved rules
     * @throws NotFoundException if a rule with the specified ID is not found
     */
    @Override
    public List<RuleGetResponse> getRules(Long id, String factName) {
        if (id != null) {
            Rule rule = ruleRepository.findById(id).orElseThrow(
                    () -> new NotFoundException(String.format("Rule with id %s not found", id)));
            return List.of(Parser.toRuleGetResponse(rule));
        }

        if (factName != null) {
            return ruleRepository.findAllByFact(factName).stream().map(Parser::toRuleGetResponse).collect(Collectors.toList());
        }

        return ruleRepository.findAll().stream().map(Parser::toRuleGetResponse).collect(Collectors.toList());
    }

    /**
     * Deletes rules based on the provided list of rule IDs.
     *
     * @param ids the list of rule IDs to delete
     * @return a list of responses containing the details of the deleted rules
     * @throws NotFoundException if a rule with the specified ID is not found
     */
    @Override
    @Transactional
    public List<RuleDeleteResponse> deleteRule(List<Long> ids) {
        final List<RuleDeleteResponse> response = new ArrayList<>();

        for (Long id : ids) {
            final Rule rule = ruleRepository.findById(id).orElseThrow(
                    () -> new NotFoundException(String.format("Rule with id %s not found", id)));
            ruleRepository.delete(rule);

            log.debug("Rule deleted: {}", rule);
            response.add(Parser.toRuleDeleteResponse(rule));
        }

        return response;
    }

    @Override
    public List<EvaluateRuleResponse> evaluateRules(EvaluateRulesRequest request) {
        final Dish dish = dishRepository.findById(request.getDishId()).orElseThrow(
                () -> new NotFoundException(String.format("Dish with id %s not found", request.getDishId())));

        final Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(
                () -> new NotFoundException(String.format("Customer with id %s not found", request.getCustomerId())));


        // Create payload
        final com.iamnirvan.restaurant.core.models.requests.fuse.rules.EvaluateRulesRequest fuseRequest = Parser.toEvaluateRulesFuseRequest(dish, customer);
        String payload;
        try {
            payload = objMapper.writeValueAsString(fuseRequest);
        } catch (JsonProcessingException e) {
            log.error("Error while converting object to JSON", e);
            throw new ServerException("failed to evaluate rules");
        }

        // Create request object
        final Request newRequest = new Request.Builder()
                .url(String.format("%s/v1/evaluate/rule", ruleEngineBaseUrl))
                .post(RequestBody.create(payload, MediaType.parse("application/json; charset=utf-8")))
                .addHeader("Content-Type", "application/json")
                .build();

        // Send request and handle response
        try (Response response = httpClient.newCall(newRequest).execute()) {
            String responseBody = "";
            if (response.body() != null) {
                responseBody = response.body().string();
            }

            if (response.code() == 200) {
                return objMapper.readValue(responseBody, new com.fasterxml.jackson.core.type.TypeReference<>() {});
            } else {
                throw new ServerException("Error while analysing sentiment");
            }
        } catch (Exception e) {
            log.error("Error while analysing sentiment", e);
            throw new ServerException("Error while analysing sentiment");
        }
    }

    public static class Parser {
        public static RuleCreateResponse toRuleCreateResponse(@NotNull Rule rule) {
            return RuleCreateResponse.builder()
                    .id(rule.getId())
                    .factName(rule.getFact())
                    .ruleName(rule.getRuleName())
                    .rule(rule.getRule())
                    .created(rule.getCreated())
                    .build();
        }

        public static RuleUpdateResponse toRuleUpdateResponse(@NotNull Rule rule) {
            return RuleUpdateResponse.builder()
                    .id(rule.getId())
                    .ruleName(rule.getRuleName())
                    .rule(rule.getRule())
                    .created(rule.getCreated())
                    .build();
        }

        public static RuleGetResponse toRuleGetResponse(@NotNull Rule rule) {
            return RuleGetResponse.builder()
                    .id(rule.getId())
                    .factName(rule.getFact())
                    .ruleName(rule.getRuleName())
                    .rule(rule.getRule())
                    .created(rule.getCreated())
                    .build();
        }

        public static RuleDeleteResponse toRuleDeleteResponse(@NotNull Rule rule) {
            return RuleDeleteResponse.builder()
                    .id(rule.getId())
                    .factName(rule.getFact())
                    .ruleName(rule.getRuleName())
                    .rule(rule.getRule())
                    .created(rule.getCreated())
                    .build();
        }

        public static com.iamnirvan.restaurant.core.models.requests.fuse.rules.EvaluateRulesRequest toEvaluateRulesFuseRequest(@NotNull Dish dish, @NotNull Customer customer) {
            return com.iamnirvan.restaurant.core.models.requests.fuse.rules.EvaluateRulesRequest.builder()
                    .dish(com.iamnirvan.restaurant.core.models.requests.fuse.rules.DishDetails.builder()
                            .id(dish.getId())
                            .name(dish.getName())
                            .description(dish.getDescription())
                            .ingredients(dish.getIngredients())
                            .build())
                    .customer(com.iamnirvan.restaurant.core.models.requests.fuse.rules.CustomerDetails.builder()
                            .id(customer.getId())
                            .firstName(customer.getFirstName())
                            .lastName(customer.getLastName())
                            .allergens(customer.getAllergens().stream()
                                    .map(CustomerAllergen::getName)
                                    .collect(Collectors.toList()))
                            .build())
                    .responses(new ArrayList<>())
                    .build();
        }
    }
}
