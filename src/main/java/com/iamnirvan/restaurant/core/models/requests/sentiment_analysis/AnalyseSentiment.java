package com.iamnirvan.restaurant.core.models.requests.sentiment_analysis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyseSentiment {
    private ArrayList<String> text;
}
