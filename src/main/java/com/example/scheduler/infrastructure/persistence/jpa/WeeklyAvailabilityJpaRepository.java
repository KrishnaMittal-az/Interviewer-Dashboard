package com.example.scheduler.infrastructure.persistence.jpa;

import com.example.scheduler.infrastructure.persistence.entity.WeeklyAvailabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeeklyAvailabilityJpaRepository extends JpaRepository<WeeklyAvailabilityEntity, Long> {
    List<WeeklyAvailabilityEntity> findByInterviewerId(Long interviewerId);
    void deleteByInterviewerId(Long interviewerId);
}

