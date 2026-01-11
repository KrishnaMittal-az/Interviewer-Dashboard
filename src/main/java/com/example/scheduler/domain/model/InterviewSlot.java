package com.example.scheduler.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class InterviewSlot {
    Long id;
    Long interviewerId;
    LocalDateTime startTs;
    LocalDateTime endTs;
    int capacity;
    SlotStatus status;
    int version;

    public boolean isOpen() {
        return status == SlotStatus.OPEN;
    }
}

