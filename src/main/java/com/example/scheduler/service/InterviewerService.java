package com.example.scheduler.service;

import com.example.scheduler.domain.model.Interviewer;
import com.example.scheduler.domain.model.WeeklyAvailability;
import com.example.scheduler.domain.repository.InterviewerPort;
import com.example.scheduler.domain.repository.WeeklyAvailabilityPort;
import com.example.scheduler.dto.AvailabilityWindowRequest;
import com.example.scheduler.dto.CreateInterviewerRequest;
import com.example.scheduler.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewerService {

    private final InterviewerPort interviewerPort;
    private final WeeklyAvailabilityPort availabilityPort;
    private final com.example.scheduler.service.SlotGenerationService slotGenerationService;
    private final com.example.scheduler.domain.repository.InterviewSlotPort slotPort;

    public Interviewer createInterviewer(CreateInterviewerRequest req) {
        Interviewer interviewer = Interviewer.builder()
                .name(req.getName())
                .weeklyCapacity(req.getWeeklyCapacity())
                .build();
        return interviewerPort.save(interviewer);
    }

    @Transactional
    public List<WeeklyAvailability> setAvailability(Long interviewerId, List<AvailabilityWindowRequest> windows) {
        Interviewer interviewer = interviewerPort.findById(interviewerId)
                .orElseThrow(() -> new NotFoundException("Interviewer not found"));
        availabilityPort.deleteByInterviewerId(interviewer.getId());
        return windows.stream().map(w -> availabilityPort.save(
                WeeklyAvailability.builder()
                        .interviewerId(interviewer.getId())
                        .dayOfWeek(w.getDayOfWeek())
                        .startTime(w.getStartTime())
                        .endTime(w.getEndTime())
                        .slotDurationMinutes(w.getSlotDurationMinutes())
                        .build()
        )).collect(Collectors.toList());
    }

        @Transactional
        public WeeklyAvailability updateAvailability(Long interviewerId, Long availabilityId, AvailabilityWindowRequest w) {
        Interviewer interviewer = interviewerPort.findById(interviewerId)
            .orElseThrow(() -> new NotFoundException("Interviewer not found"));
        var existing = availabilityPort.findById(availabilityId)
            .orElseThrow(() -> new NotFoundException("Availability not found"));
        if (!existing.getInterviewerId().equals(interviewer.getId())) {
            throw new NotFoundException("Availability does not belong to interviewer");
        }
        WeeklyAvailability updated = WeeklyAvailability.builder()
            .id(existing.getId())
            .interviewerId(interviewer.getId())
            .dayOfWeek(w.getDayOfWeek())
            .startTime(w.getStartTime())
            .endTime(w.getEndTime())
            .slotDurationMinutes(w.getSlotDurationMinutes())
            .build();
        WeeklyAvailability saved = availabilityPort.save(updated);
        // refresh slots: remove open slots in next 90 days and regenerate for next 30
        java.time.LocalDateTime from = java.time.LocalDateTime.now();
        java.time.LocalDateTime to = from.plusDays(90);
        slotPort.deleteOpenBetween(interviewerId, from, to);
        slotGenerationService.generateForInterviewer(interviewerId, 30);
        return saved;
        }

        @Transactional
        public void deleteAvailability(Long interviewerId, Long availabilityId) {
        Interviewer interviewer = interviewerPort.findById(interviewerId)
            .orElseThrow(() -> new NotFoundException("Interviewer not found"));
        var existing = availabilityPort.findById(availabilityId)
            .orElseThrow(() -> new NotFoundException("Availability not found"));
        if (!existing.getInterviewerId().equals(interviewer.getId())) {
            throw new NotFoundException("Availability does not belong to interviewer");
        }
        availabilityPort.deleteById(availabilityId);
        // remove existing open slots for the next 90 days
        java.time.LocalDateTime from = java.time.LocalDateTime.now();
        java.time.LocalDateTime to = from.plusDays(90);
        slotPort.deleteOpenBetween(interviewerId, from, to);
        // regenerate slots for next 30 days based on remaining availability, if any
        List<WeeklyAvailability> remaining = availabilityPort.findByInterviewerId(interviewerId);
        if (!remaining.isEmpty()) {
            slotGenerationService.generateForInterviewer(interviewerId, 30);
        }
        }

    public Interviewer updateWeeklyCapacity(Long interviewerId, int capacity) {
        Interviewer interviewer = interviewerPort.findById(interviewerId)
                .orElseThrow(() -> new NotFoundException("Interviewer not found"));
        Interviewer updated = Interviewer.builder()
                .id(interviewer.getId())
                .name(interviewer.getName())
                .weeklyCapacity(capacity)
                .build();
        return interviewerPort.save(updated);
    }

    public List<WeeklyAvailability> listAvailability(Long interviewerId) {
        return availabilityPort.findByInterviewerId(interviewerId);
    }
}

