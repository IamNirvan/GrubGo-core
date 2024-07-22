package com.iamnirvan.restaurant.core.services.impl;

import com.iamnirvan.restaurant.core.exceptions.BadRequestException;
import com.iamnirvan.restaurant.core.exceptions.NotFoundException;
import com.iamnirvan.restaurant.core.models.entities.Customer;
import com.iamnirvan.restaurant.core.models.entities.Dish;
import com.iamnirvan.restaurant.core.models.entities.Review;
import com.iamnirvan.restaurant.core.models.requests.review.ReviewCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.review.ReviewUpdateRequest;
import com.iamnirvan.restaurant.core.models.responses.review.ReviewCreateResponse;
import com.iamnirvan.restaurant.core.models.responses.review.ReviewDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.review.ReviewUpdateResponse;
import com.iamnirvan.restaurant.core.repositories.CustomerRepository;
import com.iamnirvan.restaurant.core.repositories.DishRepository;
import com.iamnirvan.restaurant.core.repositories.ReviewRepository;
import com.iamnirvan.restaurant.core.services.IReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReviewService implements IReviewService {
    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;
    private final DishRepository dishRepository;

    /**
     * Create multiple reviews using a list of create requests
     *
     * @param requests list of create requests
     * @return list of create responses
     * @throws NotFoundException if customer id or dish id does not exist
     */
    @Override
    public List<ReviewCreateResponse> createReview(List<ReviewCreateRequest> requests) throws NotFoundException{
        final List<ReviewCreateResponse> result = new ArrayList<>();

        for (ReviewCreateRequest request : requests) {

            Customer customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new NotFoundException(String.format("Customer with id %s does not exist", request.getCustomerId())));

            Dish dish = dishRepository.findById(request.getDishId())
                    .orElseThrow(() -> new NotFoundException(String.format("Dish with id %s does not exist", request.getDishId())));

            Review review = Review.builder()
                    .title(request.getTitle())
                    .content(request.getContent())
                    .rating(request.getRating())
                    .customer(customer)
                    .dish(dish)
                    .created(OffsetDateTime.now())
                    .build();
            reviewRepository.save(review);
            log.debug(String.format("Review created: %s", review));

            result.add(ReviewCreateResponse.builder()
                    .id(review.getId())
                    .title(review.getTitle())
                    .content(review.getContent())
                    .rating(review.getRating())
                    .customerId(review.getCustomer().getId())
                    .dishId(review.getDish().getId())
                    .created(review.getCreated())
                    .updated(review.getUpdated())
                    .build());
        }
        return result;
    }

    /**
     * Update multiple reviews using a list of update requests
     *
     * @param requests list of update requests
     * @return list of update responses
     * @throws NotFoundException   if review is not found
     * @throws BadRequestException if supplied fields are invalid
     */
    @Transactional
    @Override
    public List<ReviewUpdateResponse> updateReview(List<ReviewUpdateRequest> requests) throws NotFoundException, BadRequestException {
        final List<ReviewUpdateResponse> result = new ArrayList<>();

        for (ReviewUpdateRequest request : requests) {
            Review review = reviewRepository.findById(request.getId())
                    .orElseThrow(() -> new NotFoundException(String.format("Review with id %s does not exist", request.getId())));


            if (request.getTitle() != null) {
                if (request.getTitle().isEmpty()) {
                    throw new BadRequestException("Title cannot be empty");
                }
                review.setTitle(request.getTitle());
            }

            if (request.getContent() != null) {
                if (request.getContent().isEmpty()) {
                    throw new BadRequestException("Content cannot be empty");
                }
                review.setContent(request.getContent());
            }

            if (request.getRating() != null) {
                review.setTitle(request.getTitle());
            }

            review.setUpdated(OffsetDateTime.now());
            reviewRepository.save(review);
            log.debug(String.format("Review updated: %s", review));

            result.add(ReviewUpdateResponse.builder()
                    .id(review.getId())
                    .title(review.getTitle())
                    .content(review.getContent())
                    .rating(review.getRating())
                    .created(review.getCreated())
                    .updated(review.getUpdated())
                    .build());
        }
        return result;
    }

    /**
     * Delete multiple reviews using a list of ids
     *
     * @param ids list of ids
     * @return list of delete responses
     * @throws NotFoundException if review is not found
     */
    @Transactional
    @Override
    public List<ReviewDeleteResponse> deleteReview(List<Long> ids) throws NotFoundException {
        final List<ReviewDeleteResponse> result = new ArrayList<>();

        for (Long id : ids) {
            Review review = reviewRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("Review with id %s does not exist", id)));

            reviewRepository.delete(review);
            log.debug(String.format("Review deleted: %s", review));

            result.add(ReviewDeleteResponse.builder()
                    .id(review.getId())
                    .title(review.getTitle())
                    .content(review.getContent())
                    .rating(review.getRating())
                    .created(review.getCreated())
                    .updated(review.getUpdated())
                    .build());
        }
        return result;
    }

    /**
     * Get reviews
     *
     * @param id review id
     * @param dishId dish id
     * @return list of reviews
     * @throws NotFoundException if dish is not found
     */
    @Override
    public List<Review> getReviews(Long id, Long dishId) throws NotFoundException {
        if (id != null) {
            Review address = reviewRepository.findById(id).orElseThrow(() ->
                    new NotFoundException((String.format("Review with id %s does not exist", id))));
            return List.of(address);
        } else if (dishId != null) {
            if (dishRepository.findById(dishId).isEmpty()) {
                throw new NotFoundException(String.format("Dish with id %s does not exist", dishId));
            }
            return reviewRepository.findReviewsByDishId(dishId);
        }
        return reviewRepository.findAll();
    }
}
