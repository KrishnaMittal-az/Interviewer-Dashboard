package com.example.scheduler.service;

import com.example.scheduler.domain.model.*;
import com.example.scheduler.domain.repository.*;
import com.example.scheduler.dto.BookingResponse;
import com.example.scheduler.exception.ConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class BookingServiceTest {

    private InterviewSlotPort slotPort;
    private InterviewBookingPort bookingPort;
    private CandidatePort candidatePort;
    private InterviewerPort interviewerPort;
    private SlotGenerationService generationService;
    private BookingService bookingService;

    @BeforeEach
    void setup() {
        slotPort = Mockito.mock(InterviewSlotPort.class);
        bookingPort = Mockito.mock(InterviewBookingPort.class);
        candidatePort = Mockito.mock(CandidatePort.class);
        interviewerPort = Mockito.mock(InterviewerPort.class);
        WeeklyAvailabilityPort availabilityPort = Mockito.mock(WeeklyAvailabilityPort.class);
        generationService = new SlotGenerationService(interviewerPort, availabilityPort, slotPort);
        bookingService = new BookingService(slotPort, bookingPort, candidatePort, interviewerPort, generationService);
    }

    @Test
    void preventsWeeklyOverbooking() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        InterviewSlot slot = InterviewSlot.builder()
                .id(1L).interviewerId(2L).startTs(start).endTs(start.plusMinutes(30))
                .capacity(1).status(SlotStatus.OPEN).version(0).build();
        when(candidatePort.findById(3L)).thenReturn(Optional.of(Candidate.builder().id(3L).name("c").email("e").build()));
        when(slotPort.findForUpdate(1L)).thenReturn(Optional.of(slot));
        when(candidatePort.hasActiveBooking(3L)).thenReturn(false);
        when(interviewerPort.findById(2L)).thenReturn(Optional.of(Interviewer.builder().id(2L).name("i").weeklyCapacity(0).build()));
        // weekly capacity 0 should fail
        when(bookingPort.countActiveBookingsForWeek(anyLong(), any(), any())).thenReturn(0L);

        assertThrows(ConflictException.class, () -> bookingService.bookSlot(1L, 3L));
    }

    @Test
    void booksWhenAvailable() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        InterviewSlot slot = InterviewSlot.builder()
                .id(1L).interviewerId(2L).startTs(start).endTs(start.plusMinutes(30))
                .capacity(1).status(SlotStatus.OPEN).version(0).build();
        when(candidatePort.findById(3L)).thenReturn(Optional.of(Candidate.builder().id(3L).name("c").email("e").build()));
        when(slotPort.findForUpdate(1L)).thenReturn(Optional.of(slot));
        when(candidatePort.hasActiveBooking(3L)).thenReturn(false);
        when(interviewerPort.findById(2L)).thenReturn(Optional.of(Interviewer.builder().id(2L).name("i").weeklyCapacity(2).build()));
        when(bookingPort.countActiveBookingsForWeek(anyLong(), any(), any())).thenReturn(0L);
        when(bookingPort.save(any())).thenAnswer(inv -> {
            InterviewBooking b = inv.getArgument(0);
            return InterviewBooking.builder()
                    .id(10L)
                    .candidateId(b.getCandidateId())
                    .slotId(b.getSlotId())
                    .status(b.getStatus())
                    .bookedAt(b.getBookedAt())
                    .version(0)
                    .build();
        });
        when(slotPort.save(any())).thenReturn(slot);

        BookingResponse response = bookingService.bookSlot(1L, 3L);
        assertThat(response.getBookingId()).isEqualTo(10L);
    }
}

