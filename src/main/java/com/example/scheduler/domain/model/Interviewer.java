package com.example.scheduler.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Interviewer {
    Long id;
    String name;
    int weeklyCapacity;
}

