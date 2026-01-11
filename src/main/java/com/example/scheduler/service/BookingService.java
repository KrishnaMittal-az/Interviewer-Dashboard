package com.example.scheduler.service;

import com.example.scheduler.domain.model.*;
import com.example.scheduler.domain.repository.*;
import com.example.scheduler.dto.BookingResponse;
import com.example.scheduler.exception.BadRequestException;
import com.example.scheduler.exception.ConflictException;
import com.example.scheduler.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final InterviewSlotPort slotPort;
    private final InterviewBookingPort bookingPort;
    private final CandidatePort candidatePort;
    private final InterviewerPort interviewerPort;
    private final SlotGenerationService generationService;

    @Transactional
    public BookingResponse bookSlot(Long slotId, Long candidateId) {
        Candidate candidate = candidatePort.findById(candidateId)
                .orElseThrow(() -> new NotFoundException("Candidate not found"));
        InterviewSlot slot = slotPort.findForUpdate(slotId)
                .orElseThrow(() -> new NotFoundException("Slot not found"));
        if (!slot.isOpen() || slot.getStartTs().isBefore(LocalDateTime.now())) {
            throw new ConflictException("Slot not available");
        }
        if (candidatePort.hasActiveBooking(candidate.getId())) {
            throw new ConflictException("Candidate already has a booking");
        }
        Interviewer interviewer = interviewerPort.findById(slot.getInterviewerId())
                .orElseThrow(() -> new NotFoundException("Interviewer not found"));
        SlotGenerationService.WeekRange week = generationService.weekRange(slot.getStartTs());
        long activeCount = bookingPort.countActiveBookingsForWeek(interviewer.getId(), week.start(), week.end());
        if (activeCount >= interviewer.getWeeklyCapacity()) {
            throw new ConflictException("Weekly interview capacity exceeded");
        }
        if (slot.getCapacity() <= 0) {
            throw new ConflictException("Slot fully booked");
        }
        InterviewBooking booking = bookingPort.save(InterviewBooking.builder()
                .candidateId(candidate.getId())
                .slotId(slot.getId())
                .status(BookingStatus.BOOKED)
                .bookedAt(LocalDateTime.now())
                .version(0)
                .build());
        slotPort.save(InterviewSlot.builder()
                .id(slot.getId())
                .interviewerId(slot.getInterviewerId())
                .startTs(slot.getStartTs())
                .endTs(slot.getEndTs())
                .capacity(slot.getCapacity() - 1)
                .status(slot.getCapacity() - 1 == 0 ? SlotStatus.BOOKED : slot.getStatus())
                .version(slot.getVersion())
                .build());

        return BookingResponse.builder()
                .bookingId(booking.getId())
                .slotId(slot.getId())
                .candidateId(candidate.getId())
                .status(booking.getStatus().name())
                .build();
    }

    @Transactional
    public BookingResponse reschedule(Long bookingId, Long newSlotId) {
        InterviewBooking booking = bookingPort.findForUpdate(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        if (booking.getStatus() != BookingStatus.BOOKED) {
            throw new BadRequestException("Only active bookings can be rescheduled");
        }

        InterviewSlot oldSlot = slotPort.findForUpdate(booking.getSlotId())
                .orElseThrow(() -> new NotFoundException("Old slot missing"));
        InterviewSlot newSlot = slotPort.findForUpdate(newSlotId)
                .orElseThrow(() -> new NotFoundException("New slot not found"));
        if (!newSlot.isOpen() || newSlot.getCapacity() <= 0) {
            throw new ConflictException("New slot not available");
        }

        Interviewer interviewer = interviewerPort.findById(newSlot.getInterviewerId())
                .orElseThrow(() -> new NotFoundException("Interviewer not found"));
        SlotGenerationService.WeekRange week = generationService.weekRange(newSlot.getStartTs());
        long activeCount = bookingPort.countActiveBookingsForWeek(interviewer.getId(), week.start(), week.end());
        if (activeCount >= interviewer.getWeeklyCapacity()) {
            throw new ConflictException("Weekly interview capacity exceeded");
        }

        // adjust capacities
        slotPort.save(InterviewSlot.builder()
                .id(oldSlot.getId())
                .interviewerId(oldSlot.getInterviewerId())
                .startTs(oldSlot.getStartTs())
                .endTs(oldSlot.getEndTs())
                .capacity(oldSlot.getCapacity() + 1)
                .status(SlotStatus.OPEN)
                .version(oldSlot.getVersion())
                .build());

        slotPort.save(InterviewSlot.builder()
                .id(newSlot.getId())
                .interviewerId(newSlot.getInterviewerId())
                .startTs(newSlot.getStartTs())
                .endTs(newSlot.getEndTs())
                .capacity(newSlot.getCapacity() - 1)
                .status(newSlot.getCapacity() - 1 == 0 ? SlotStatus.BOOKED : newSlot.getStatus())
                .version(newSlot.getVersion())
                .build());

        InterviewBooking updated = bookingPort.save(InterviewBooking.builder()
                .id(booking.getId())
                .candidateId(booking.getCandidateId())
                .slotId(newSlot.getId())
                .status(BookingStatus.BOOKED)
                .bookedAt(booking.getBookedAt())
                .version(booking.getVersion())
                .build());

        return BookingResponse.builder()
                .bookingId(updated.getId())
                .slotId(updated.getSlotId())
                .candidateId(updated.getCandidateId())
                .status(updated.getStatus().name())
                .build();
    }
}

