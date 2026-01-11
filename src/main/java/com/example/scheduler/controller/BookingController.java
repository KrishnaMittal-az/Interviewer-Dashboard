package com.example.scheduler.controller;

import com.example.scheduler.dto.BookSlotRequest;
import com.example.scheduler.dto.BookingResponse;
import com.example.scheduler.dto.RescheduleRequest;
import com.example.scheduler.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/slot/{slotId}")
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse book(@PathVariable Long slotId, @RequestBody @Valid BookSlotRequest request) {
        return bookingService.bookSlot(slotId, request.getCandidateId());
    }

    @PatchMapping("/{bookingId}/reschedule")
    public BookingResponse reschedule(@PathVariable Long bookingId, @RequestBody @Valid RescheduleRequest request) {
        return bookingService.reschedule(bookingId, request.getNewSlotId());
    }
}

