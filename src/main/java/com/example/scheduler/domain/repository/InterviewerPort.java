package com.example.scheduler.domain.repository;

import com.example.scheduler.domain.model.Interviewer;

import java.util.Optional;

public interface InterviewerPort {
    Interviewer save(Interviewer interviewer);
    Optional<Interviewer> findById(Long id);
}

