package com.iamnirvan.restaurant.core.models.responses.analyse_sentiment;

import com.iamnirvan.restaurant.core.models.responses.dish_portion.DishPortionGetResponseWithoutDishName;
import com.iamnirvan.restaurant.core.models.responses.review.ReviewGetResponseWithoutDishId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnalyseSentimentResponse {
    private String span;
    private Float positiveRatio;
    private Float neutralRatio;
    private Float negativeRatio;
}
