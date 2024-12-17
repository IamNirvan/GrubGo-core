package com.iamnirvan.restaurant.core.models.responses.metrics;

public interface UnitsSoldPerMonthProjection {
    String getMonth();
    Long getUnitsSold();
}
