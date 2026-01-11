package com.example.scheduler.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class InterviewBooking {
    Long id;
    Long candidateId;
    Long slotId;
    BookingStatus status;
    LocalDateTime bookedAt;
    int version;
}

