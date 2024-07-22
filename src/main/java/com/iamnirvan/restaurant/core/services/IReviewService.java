package com.iamnirvan.restaurant.core.services;

import com.iamnirvan.restaurant.core.models.entities.Review;
import com.iamnirvan.restaurant.core.models.requests.review.ReviewCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.review.ReviewUpdateRequest;
import com.iamnirvan.restaurant.core.models.responses.review.ReviewCreateResponse;
import com.iamnirvan.restaurant.core.models.responses.review.ReviewDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.review.ReviewUpdateResponse;

import java.util.List;

public interface IReviewService {
    /**
     * Create multiple reviews using a list of create requests
     * @param requests list of create requests
     * @return list of create responses
     * */
    List<ReviewCreateResponse> createReview(List<ReviewCreateRequest> requests);

    /**
     * Update multiple reviews using a list of update requests
     * @param requests list of update requests
     * @return list of update responses
     * */
    List<ReviewUpdateResponse> updateReview(List<ReviewUpdateRequest> requests);

    /**
     * Delete multiple reviews using a list of ids
     * @param ids list of ids
     * @return list of delete responses
     * */
    List<ReviewDeleteResponse> deleteReview(List<Long> ids);

    /**
     * Get reviews
     * @param id review id
     * @param dishId dish id
     * @return list of reviews
     * */
    List<Review> getReviews(Long id, Long dishId);
}
