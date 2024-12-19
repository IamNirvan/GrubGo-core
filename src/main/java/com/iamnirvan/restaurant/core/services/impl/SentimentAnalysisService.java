package com.iamnirvan.restaurant.core.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.iamnirvan.restaurant.core.exceptions.NotFoundException;
import com.iamnirvan.restaurant.core.models.entities.*;
import com.iamnirvan.restaurant.core.models.requests.sentiment_analysis.AnalyseSentiment;
import com.iamnirvan.restaurant.core.models.responses.analyse_sentiment.AnalyseSentimentResponse;
import com.iamnirvan.restaurant.core.repositories.*;
import com.iamnirvan.restaurant.core.services.ISentimentAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class SentimentAnalysisService implements ISentimentAnalysisService {
    @Value("${sentiment.analysis.url}")
    private String sentimentAnalysisBaseUrl;
    private final ReviewRepository reviewRepository;
    private final DishRepository dishRepository;
    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public List<AnalyseSentimentResponse> analyseSentimentInDishReviews(Long dishId) {
        dishRepository.findById(dishId).orElseThrow(() -> new NotFoundException("Dish not found"));

        List<Review> reviews = reviewRepository.findReviewsByDishId(dishId);
        ArrayList<String> reviewsToAnalyse = new ArrayList<>();
        for (Review review : reviews) {
            reviewsToAnalyse.add(review.getContent());
        }

        // Create payload
        String payload;
        try {
            payload = objMapper.writeValueAsString(new AnalyseSentiment(reviewsToAnalyse));
        } catch (JsonProcessingException e) {
            log.error("Error while converting object to JSON", e);
            throw new RuntimeException(e);
        }

        // Create request object
        final Request request = new Request.Builder()
                .url(String.format("%s/v2/analyse/sentiment", sentimentAnalysisBaseUrl))
                .post(RequestBody.create(payload, MediaType.parse("application/json; charset=utf-8")))
                .addHeader("Content-Type", "application/json")
                .build();

        // Send request and handle response
        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = "";
            if (response.body() != null) {
                responseBody = response.body().string();
            }

            if (response.code() == 200) {
                return objMapper.readValue(responseBody, new com.fasterxml.jackson.core.type.TypeReference<>() {});
            } else {
                throw new RuntimeException("Error while analysing sentiment");
            }
        } catch (Exception e) {
            log.error("Error while analysing sentiment", e);
            throw new RuntimeException(e);
        }
    }
}
