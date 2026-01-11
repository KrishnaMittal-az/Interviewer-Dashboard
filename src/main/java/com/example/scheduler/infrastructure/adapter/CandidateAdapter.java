package com.example.scheduler.infrastructure.adapter;

import com.example.scheduler.domain.model.BookingStatus;
import com.example.scheduler.domain.model.Candidate;
import com.example.scheduler.domain.repository.CandidatePort;
import com.example.scheduler.infrastructure.mapper.CandidateMapper;
import com.example.scheduler.infrastructure.persistence.entity.CandidateEntity;
import com.example.scheduler.infrastructure.persistence.jpa.CandidateJpaRepository;
import com.example.scheduler.infrastructure.persistence.jpa.InterviewBookingJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CandidateAdapter implements CandidatePort {

    private final CandidateJpaRepository repository;
    private final InterviewBookingJpaRepository bookingRepository;
    private final CandidateMapper mapper;

    @Override
    public Candidate save(Candidate candidate) {
        CandidateEntity entity = mapper.toEntity(candidate);
        if (candidate.getId() != null) {
            entity.setId(candidate.getId());
        }
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<Candidate> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean hasActiveBooking(Long candidateId) {
        return bookingRepository.existsByCandidateIdAndStatus(candidateId, BookingStatus.BOOKED);
    }
}

