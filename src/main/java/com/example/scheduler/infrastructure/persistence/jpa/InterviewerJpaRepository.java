package com.example.scheduler.infrastructure.persistence.jpa;

import com.example.scheduler.infrastructure.persistence.entity.InterviewerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewerJpaRepository extends JpaRepository<InterviewerEntity, Long> {
}

