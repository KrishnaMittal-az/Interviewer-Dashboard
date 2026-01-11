package com.example.scheduler.controller;

import com.example.scheduler.domain.model.Interviewer;
import com.example.scheduler.domain.model.WeeklyAvailability;
import com.example.scheduler.dto.AvailabilityWindowRequest;
import com.example.scheduler.dto.CreateInterviewerRequest;
import com.example.scheduler.service.InterviewerService;
import com.example.scheduler.service.SlotGenerationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/interviewers")
@RequiredArgsConstructor
public class InterviewerController {

    private final InterviewerService interviewerService;
    private final SlotGenerationService slotGenerationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Interviewer create(@RequestBody @Valid CreateInterviewerRequest request) {
        return interviewerService.createInterviewer(request);
    }

    @PutMapping("/{id}/availability")
    public List<WeeklyAvailability> setAvailability(@PathVariable Long id,
                                                    @RequestBody @Valid List<AvailabilityWindowRequest> windows) {
        return interviewerService.setAvailability(id, windows);
    }

    @PutMapping("/{id}/capacity/{capacity}")
    public Interviewer updateCapacity(@PathVariable Long id, @PathVariable int capacity) {
        return interviewerService.updateWeeklyCapacity(id, capacity);
    }

    @PostMapping("/{id}/slots/generate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void generateSlots(@PathVariable Long id, @RequestParam(required = false) Integer days) {
        slotGenerationService.generateForInterviewer(id, days);
    }
}

