package com.example.scheduler.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalTime;

@Value
@Builder
public class WeeklyAvailability {
    Long id;
    Long interviewerId;
    int dayOfWeek; // 1 = Monday
    LocalTime startTime;
    LocalTime endTime;
    int slotDurationMinutes;
}

