package com.example.scheduler.service;

import com.example.scheduler.domain.model.Candidate;
import com.example.scheduler.domain.repository.CandidatePort;
import com.example.scheduler.dto.CandidateRequest;
import com.example.scheduler.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidatePort candidatePort;

    public Candidate create(CandidateRequest request) {
        if (candidatePort.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        Candidate candidate = Candidate.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();
        return candidatePort.save(candidate);
    }

    public java.util.Optional<com.example.scheduler.domain.model.Candidate> findById(Long id) {
        return candidatePort.findById(id);
    }
}

