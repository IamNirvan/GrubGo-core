package com.iamnirvan.restaurant.core.services;

import com.iamnirvan.restaurant.core.models.requests.customer.CustomerRegisterRequest;
import com.iamnirvan.restaurant.core.models.requests.customer.CustomerUpdateRequest;
import com.iamnirvan.restaurant.core.models.responses.analyse_sentiment.AnalyseSentimentResponse;
import com.iamnirvan.restaurant.core.models.responses.customer.CustomerDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.customer.CustomerGetResponse;
import com.iamnirvan.restaurant.core.models.responses.customer.CustomerRegisterResponse;
import com.iamnirvan.restaurant.core.models.responses.customer.CustomerUpdateResponse;

import java.util.List;

public interface ISentimentAnalysisService {
    List<AnalyseSentimentResponse> analyseSentimentInDishReviews(Long dishId);
}
