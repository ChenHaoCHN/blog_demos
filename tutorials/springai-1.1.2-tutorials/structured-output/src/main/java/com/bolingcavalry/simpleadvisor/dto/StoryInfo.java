package com.bolingcavalry.simpleadvisor.dto;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record StoryInfo(
    
    @JsonPropertyDescription("事件年代") 
    int age,
    
    @JsonPropertyDescription("事件") 
    String event
) {

}
