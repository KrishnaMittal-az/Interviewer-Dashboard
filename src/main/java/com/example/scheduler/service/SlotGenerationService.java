package com.example.scheduler.service;

import com.example.scheduler.domain.model.InterviewSlot;
import com.example.scheduler.domain.model.Interviewer;
import com.example.scheduler.domain.model.SlotStatus;
import com.example.scheduler.domain.model.WeeklyAvailability;
import com.example.scheduler.domain.repository.InterviewSlotPort;
import com.example.scheduler.domain.repository.InterviewerPort;
import com.example.scheduler.domain.repository.WeeklyAvailabilityPort;
import com.example.scheduler.exception.BadRequestException;
import com.example.scheduler.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SlotGenerationService {

    private final InterviewerPort interviewerPort;
    private final WeeklyAvailabilityPort availabilityPort;
    private final InterviewSlotPort slotPort;

    @Value("${app.slot.generation-days:14}")
    private int generationDays;

    @Transactional
    public void generateForInterviewer(Long interviewerId, Integer days) {
        Interviewer interviewer = interviewerPort.findById(interviewerId)
                .orElseThrow(() -> new NotFoundException("Interviewer not found"));
        List<WeeklyAvailability> windows = availabilityPort.findByInterviewerId(interviewerId);
        if (windows.isEmpty()) {
            // No availability configured: nothing to generate.
            return;
        }
        int horizon = days != null ? days : generationDays;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(horizon);
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            int dow = date.getDayOfWeek().getValue();
            for (WeeklyAvailability window : windows) {
                if (window.getDayOfWeek() != dow) continue;
                LocalTime start = window.getStartTime();
                while (start.isBefore(window.getEndTime())) {
                    LocalTime end = start.plusMinutes(window.getSlotDurationMinutes());
                    LocalDateTime startTs = LocalDateTime.of(date, start);
                    LocalDateTime endTs = LocalDateTime.of(date, end);
                    if (!slotPort.exists(interviewerId, startTs, endTs)) {
                        slotPort.save(InterviewSlot.builder()
                                .interviewerId(interviewerId)
                                .startTs(startTs)
                                .endTs(endTs)
                                .capacity(1) // single seat; weekly cap enforced separately
                                .status(SlotStatus.OPEN)
                                .version(0)
                                .build());
                    }
                    start = end;
                }
            }
        }
    }

    @Transactional
    public void generateForInterviewer(Long interviewerId, java.time.LocalDate startDate, java.time.LocalDate endDate) {
        Interviewer interviewer = interviewerPort.findById(interviewerId)
                .orElseThrow(() -> new NotFoundException("Interviewer not found"));
        List<WeeklyAvailability> windows = availabilityPort.findByInterviewerId(interviewerId);
        if (windows.isEmpty()) {
            // No availability configured: nothing to generate.
            return;
        }
        if (endDate.isBefore(startDate)) {
            throw new BadRequestException("endDate must be on or after startDate");
        }
        for (java.time.LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            int dow = date.getDayOfWeek().getValue();
            for (WeeklyAvailability window : windows) {
                if (window.getDayOfWeek() != dow) continue;
                java.time.LocalTime start = window.getStartTime();
                while (start.isBefore(window.getEndTime())) {
                    java.time.LocalTime end = start.plusMinutes(window.getSlotDurationMinutes());
                    java.time.LocalDateTime startTs = java.time.LocalDateTime.of(date, start);
                    java.time.LocalDateTime endTs = java.time.LocalDateTime.of(date, end);
                    if (!slotPort.exists(interviewerId, startTs, endTs)) {
                        slotPort.save(InterviewSlot.builder()
                                .interviewerId(interviewerId)
                                .startTs(startTs)
                                .endTs(endTs)
                                .capacity(1)
                                .status(SlotStatus.OPEN)
                                .version(0)
                                .build());
                    }
                    start = end;
                }
            }
        }
    }

    // Daily at 02:00
    @Scheduled(cron = "0 0 2 * * *")
    public void generateDaily() {
        // In a real system we'd iterate all interviewers; omitted for brevity.
    }

    public WeekRange weekRange(LocalDateTime dateTime) {
        LocalDate date = dateTime.toLocalDate();
        WeekFields wf = WeekFields.ISO;
        LocalDate start = date.with(wf.dayOfWeek(), 1);
        LocalDate end = start.plusDays(6);
        return new WeekRange(start.atStartOfDay(), end.atTime(LocalTime.MAX));
    }

    public record WeekRange(LocalDateTime start, LocalDateTime end) { }
}

