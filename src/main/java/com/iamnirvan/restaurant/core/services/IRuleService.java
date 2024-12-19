package com.iamnirvan.restaurant.core.services;

import com.iamnirvan.restaurant.core.models.requests.rules.RuleCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.rules.RuleUpdateRequest;
import com.iamnirvan.restaurant.core.models.requests.rules.evaluation.EvaluateRulesRequest;
import com.iamnirvan.restaurant.core.models.responses.rules.RuleCreateResponse;
import com.iamnirvan.restaurant.core.models.responses.rules.RuleDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.rules.RuleGetResponse;
import com.iamnirvan.restaurant.core.models.responses.rules.RuleUpdateResponse;
import com.iamnirvan.restaurant.core.models.responses.rules.evaluation.EvaluateRuleResponse;

import java.util.List;

public interface IRuleService {
    List<RuleCreateResponse> createRule(List<RuleCreateRequest> requests);
    List<RuleUpdateResponse> updateRule(List<RuleUpdateRequest> requests);
    List<RuleGetResponse> getRules(Long id, String factName);
    List<RuleDeleteResponse> deleteRule(List<Long> ids);
    List<EvaluateRuleResponse> evaluateRules(EvaluateRulesRequest request);
}
