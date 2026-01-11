package com.example.scheduler.infrastructure.adapter;

import com.example.scheduler.domain.model.InterviewSlot;
import com.example.scheduler.domain.model.SlotStatus;
import com.example.scheduler.domain.repository.InterviewSlotPort;
import com.example.scheduler.infrastructure.mapper.InterviewSlotMapper;
import com.example.scheduler.infrastructure.persistence.entity.InterviewSlotEntity;
import com.example.scheduler.infrastructure.persistence.jpa.InterviewSlotJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InterviewSlotAdapter implements InterviewSlotPort {

    private final InterviewSlotJpaRepository repository;
    private final InterviewSlotMapper mapper;

    @Override
    public InterviewSlot save(InterviewSlot slot) {
        InterviewSlotEntity entity = mapper.toEntity(slot);
        if (slot.getId() != null) {
            entity.setId(slot.getId());
        }
        var interviewerRef = new com.example.scheduler.infrastructure.persistence.entity.InterviewerEntity();
        interviewerRef.setId(slot.getInterviewerId());
        entity.setInterviewer(interviewerRef);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public boolean exists(Long interviewerId, LocalDateTime start, LocalDateTime end) {
        return repository.existsByInterviewerIdAndStartTsAndEndTs(interviewerId, start, end);
    }

    @Override
    public Optional<InterviewSlot> findForUpdate(Long slotId) {
        return repository.findForUpdate(slotId).map(mapper::toDomain);
    }

    @Override
    public Optional<InterviewSlot> findById(Long slotId) {
        return repository.findById(slotId).map(mapper::toDomain);
    }

    @Override
    public InterviewSlot update(InterviewSlot slot) {
        return save(slot);
    }

    @Override
    public void deleteById(Long slotId) {
        repository.deleteById(slotId);
    }

    @Override
    public List<InterviewSlot> findOpenPage(LocalDateTime from, LocalDateTime to, Long interviewerId, LocalDateTime cursorTs, Long cursorId, int pageSize) {
        return repository.findPage(SlotStatus.OPEN, from, to, interviewerId, cursorTs, cursorId, PageRequest.of(0, pageSize))
                .stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteOpenBetween(Long interviewerId, LocalDateTime from, LocalDateTime to) {
        repository.deleteByStatusAndInterviewerIdAndStartTsBetween(SlotStatus.OPEN, interviewerId, from, to);
    }
}

