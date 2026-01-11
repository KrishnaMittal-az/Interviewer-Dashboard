package com.example.scheduler.infrastructure.adapter;

import com.example.scheduler.domain.model.InterviewBooking;
import com.example.scheduler.domain.repository.InterviewBookingPort;
import com.example.scheduler.infrastructure.mapper.InterviewBookingMapper;
import com.example.scheduler.infrastructure.persistence.jpa.InterviewBookingJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InterviewBookingAdapter implements InterviewBookingPort {

    private final InterviewBookingJpaRepository repository;
    private final InterviewBookingMapper mapper;

    @Override
    public InterviewBooking save(InterviewBooking booking) {
        var entity = mapper.toEntity(booking);
        if (booking.getId() != null) {
            entity.setId(booking.getId());
        }
        var candidateRef = new com.example.scheduler.infrastructure.persistence.entity.CandidateEntity();
        candidateRef.setId(booking.getCandidateId());
        entity.setCandidate(candidateRef);
        var slotRef = new com.example.scheduler.infrastructure.persistence.entity.InterviewSlotEntity();
        slotRef.setId(booking.getSlotId());
        entity.setSlot(slotRef);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<InterviewBooking> findForUpdate(Long id) {
        return repository.findForUpdate(id).map(mapper::toDomain);
    }

    @Override
    public long countActiveBookingsForWeek(Long interviewerId, LocalDateTime weekStart, LocalDateTime weekEnd) {
        return repository.countActiveBookingsForWeek(interviewerId, weekStart, weekEnd);
    }

    @Override
    public java.util.List<com.example.scheduler.domain.model.InterviewBooking> findByInterviewerId(Long interviewerId) {
        return repository.findByInterviewerId(interviewerId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}

