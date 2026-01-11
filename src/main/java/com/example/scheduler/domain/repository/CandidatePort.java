package com.example.scheduler.domain.repository;

import com.example.scheduler.domain.model.Candidate;

import java.util.Optional;

public interface CandidatePort {
    Candidate save(Candidate candidate);
    Optional<Candidate> findById(Long id);
    boolean existsByEmail(String email);
    boolean hasActiveBooking(Long candidateId);
}

