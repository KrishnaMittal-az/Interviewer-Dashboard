package com.example.scheduler.domain.repository;

import com.example.scheduler.domain.model.WeeklyAvailability;

import java.util.List;

public interface WeeklyAvailabilityPort {
    WeeklyAvailability save(WeeklyAvailability availability);
    void deleteByInterviewerId(Long interviewerId);
    List<WeeklyAvailability> findByInterviewerId(Long interviewerId);
    java.util.Optional<WeeklyAvailability> findById(Long id);
    void deleteById(Long id);
}

