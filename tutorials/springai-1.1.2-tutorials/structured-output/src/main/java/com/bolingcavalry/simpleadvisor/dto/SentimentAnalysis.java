package com.bolingcavalry.simpleadvisor.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record SentimentAnalysis(
    @JsonPropertyDescription("情感倾向，只能为 POSITIVE/NEGATIVE/NEUTRAL") 
    String sentiment,
    
    @JsonPropertyDescription("置信度，范围 0.0-1.0") 
    double confidence,
    
    @JsonPropertyDescription("提取的关键短语列表，最多3个") 
    List<String> keyPhrases
) {}