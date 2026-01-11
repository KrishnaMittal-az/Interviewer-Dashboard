package com.example.scheduler.domain.repository;

import com.example.scheduler.domain.model.InterviewSlot;
import com.example.scheduler.domain.model.SlotStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InterviewSlotPort {
    InterviewSlot save(InterviewSlot slot);
    boolean exists(Long interviewerId, LocalDateTime start, LocalDateTime end);
    Optional<InterviewSlot> findForUpdate(Long slotId);
    Optional<InterviewSlot> findById(Long slotId);
    InterviewSlot update(InterviewSlot slot);
    void deleteById(Long slotId);
    List<InterviewSlot> findOpenPage(LocalDateTime from,
                                     LocalDateTime to,
                                     Long interviewerId,
                                     LocalDateTime cursorTs,
                                     Long cursorId,
                                     int pageSize);
    void deleteOpenBetween(Long interviewerId, LocalDateTime from, LocalDateTime to);
}

