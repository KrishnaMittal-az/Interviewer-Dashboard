package com.example.scheduler.infrastructure.persistence.jpa;

import com.example.scheduler.infrastructure.persistence.entity.CandidateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateJpaRepository extends JpaRepository<CandidateEntity, Long> {
    boolean existsByEmail(String email);
}

