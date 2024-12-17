package com.iamnirvan.restaurant.core.models.responses.metrics;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MonthlySales {
    private String month;
    private Long unitsSold;
}
