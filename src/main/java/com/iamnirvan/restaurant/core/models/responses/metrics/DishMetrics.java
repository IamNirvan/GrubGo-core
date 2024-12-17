package com.iamnirvan.restaurant.core.models.responses.metrics;

import lombok.*;

import java.util.List;

@Getter
@Builder
public class DishMetrics {
    private Long unitsSoldToday;
    private Long unitsSoldThisQuarter;
    private Float revenueAccountedFor;
    private List<MonthlySales> monthlySales;
}
