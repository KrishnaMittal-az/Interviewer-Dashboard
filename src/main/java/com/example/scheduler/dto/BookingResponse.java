package com.example.scheduler.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BookingResponse {
    Long bookingId;
    Long slotId;
    Long candidateId;
    String status;
}

