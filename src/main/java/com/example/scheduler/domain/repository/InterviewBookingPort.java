package com.example.scheduler.domain.repository;

import com.example.scheduler.domain.model.InterviewBooking;

import java.time.LocalDateTime;
import java.util.Optional;

public interface InterviewBookingPort {
    InterviewBooking save(InterviewBooking booking);
    Optional<InterviewBooking> findForUpdate(Long id);
    long countActiveBookingsForWeek(Long interviewerId, LocalDateTime weekStart, LocalDateTime weekEnd);
    java.util.List<InterviewBooking> findByInterviewerId(Long interviewerId);
}

