package com.example.scheduler.controller;

import com.example.scheduler.dto.CursorPage;
import com.example.scheduler.dto.SlotResponse;
import com.example.scheduler.service.SlotQueryService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/slots")
@RequiredArgsConstructor
public class SlotController {

    private final SlotQueryService slotQueryService;

    @GetMapping
    public CursorPage<SlotResponse> listSlots(
            @RequestParam("from")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,

            @RequestParam("to")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to,

            @RequestParam(required = false) Long interviewerId,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit
    ) {
        // Validate date range
        if (from.isAfter(to)) {
            throw new com.example.scheduler.exception.BadRequestException(
                "'from' date must be before 'to' date");
        }
        return slotQueryService.listOpenSlots(from, to, interviewerId, cursor, limit);
    }
}
