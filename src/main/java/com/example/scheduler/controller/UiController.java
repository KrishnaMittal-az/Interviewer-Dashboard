package com.example.scheduler.controller;

import com.example.scheduler.dto.AvailabilityWindowRequest;
import com.example.scheduler.dto.CandidateRequest;
import com.example.scheduler.dto.CreateInterviewerRequest;
import com.example.scheduler.service.*;
import com.example.scheduler.domain.model.SlotStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/ui")
@RequiredArgsConstructor
public class UiController {

    private final InterviewerService interviewerService;
    private final CandidateService candidateService;
    private final SlotGenerationService slotGenerationService;
    private final SlotQueryService slotQueryService;
    private final BookingService bookingService;
    private final com.example.scheduler.domain.repository.InterviewBookingPort bookingPort;
    private final com.example.scheduler.domain.repository.InterviewSlotPort slotPort;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String role,
                          @RequestParam Long id,
                          HttpSession session) {
        session.setAttribute("role", role);
        session.setAttribute("userId", id);
        if ("interviewer".equals(role)) return "redirect:/ui/interviewer/dashboard";
        return "redirect:/ui/student/dashboard";
    }

    @GetMapping("/signup/interviewer")
    public String signupInterviewerForm(Model model) {
        model.addAttribute("interviewer", new CreateInterviewerRequest());
        return "signup_interviewer";
    }

    @PostMapping("/signup/interviewer")
    public String signupInterviewer(@ModelAttribute CreateInterviewerRequest req,
                                    HttpSession session,
                                    Model model) {
        var created = interviewerService.createInterviewer(req);
        session.setAttribute("role", "interviewer");
        session.setAttribute("userId", created.getId());
        model.addAttribute("interviewerId", created.getId());
        model.addAttribute("name", created.getName());
        return "interviewer_dashboard";
    }

    @GetMapping("/signup/student")
    public String signupStudentForm(Model model) {
        model.addAttribute("candidate", new CandidateRequest());
        return "signup_student";
    }

    @PostMapping("/signup/student")
    public String signupStudent(@ModelAttribute CandidateRequest req,
                                HttpSession session,
                                Model model) {
        var created = candidateService.create(req);
        session.setAttribute("role", "student");
        session.setAttribute("userId", created.getId());
        model.addAttribute("createdId", created.getId());
        model.addAttribute("name", created.getName());
        return "student_dashboard";
    }

    @GetMapping("/interviewer/dashboard")
    public String interviewerDashboard(HttpSession session, Model model) {
        Object id = session.getAttribute("userId");
        model.addAttribute("interviewerId", id);
        // show upcoming slots for this interviewer (next 30 days)
        if (id != null) {
            java.time.LocalDateTime from = java.time.LocalDateTime.now();
            java.time.LocalDateTime to = from.plusDays(30);
            Long interviewerId = (id instanceof Long) ? (Long) id : Long.valueOf(id.toString());
            var page = slotQueryService.listOpenSlots(from, to, interviewerId, null, 100);
            model.addAttribute("slots", page.getItems());
            // list configured availability windows
            model.addAttribute("availabilities", interviewerService.listAvailability(interviewerId));
            // list bookings for interviewer and include candidate info
            var bookings = bookingPort.findByInterviewerId(interviewerId);
            var bookingViews = new java.util.ArrayList<java.util.Map<String,Object>>();
            for (var b : bookings) {
                var view = new java.util.HashMap<String,Object>();
                view.put("booking", b);
                candidateService.findById(b.getCandidateId()).ifPresent(c -> view.put("candidate", c));
                bookingViews.add(view);
            }
            model.addAttribute("bookings", bookingViews);
        }
        return "interviewer_dashboard";
    }

    @PostMapping("/interviewer/{id}/availability")
    public String setAvailability(@PathVariable Long id,
                                  @RequestParam int dayOfWeek,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
                                  @RequestParam(defaultValue = "30") int slotDurationMinutes,
                                  Model model) {
        AvailabilityWindowRequest w = new AvailabilityWindowRequest();
        w.setDayOfWeek(dayOfWeek);
        w.setStartTime(startTime);
        w.setEndTime(endTime);
        w.setSlotDurationMinutes(slotDurationMinutes);
        interviewerService.setAvailability(id, List.of(w));
        return "redirect:/ui/interviewer/dashboard";
    }

    @PostMapping("/interviewer/{id}/availability/{availabilityId}/update")
    public String updateAvailability(@PathVariable Long id,
                                     @PathVariable Long availabilityId,
                                     @RequestParam int dayOfWeek,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
                                     @RequestParam(defaultValue = "30") int slotDurationMinutes,
                                     Model model) {
        com.example.scheduler.dto.AvailabilityWindowRequest w = new com.example.scheduler.dto.AvailabilityWindowRequest();
        w.setDayOfWeek(dayOfWeek);
        w.setStartTime(startTime);
        w.setEndTime(endTime);
        w.setSlotDurationMinutes(slotDurationMinutes);
        interviewerService.updateAvailability(id, availabilityId, w);
        return "redirect:/ui/interviewer/dashboard";
    }

    @PostMapping("/interviewer/{id}/availability/{availabilityId}/delete")
    public String deleteAvailability(@PathVariable Long id,
                                     @PathVariable Long availabilityId,
                                     Model model) {
        interviewerService.deleteAvailability(id, availabilityId);
        return "redirect:/ui/interviewer/dashboard";
    }

    @PostMapping("/interviewer/{id}/generate")
    public String generateSlots(@PathVariable Long id,
                                @RequestParam(required = false) Integer days,
                                @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate startDate,
                                @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate endDate,
                                Model model) {
        if (startDate != null && endDate != null) {
            slotGenerationService.generateForInterviewer(id, startDate, endDate);
        } else {
            slotGenerationService.generateForInterviewer(id, days);
        }
        return "redirect:/ui/interviewer/dashboard";
    }

    @PostMapping("/interviewer/{interviewerId}/slot/{slotId}/cancel")
    public String cancelSlot(@PathVariable Long interviewerId,
                             @PathVariable Long slotId,
                             Model model) {
        var slotOpt = slotPort.findById(slotId);
        if (slotOpt.isPresent()) {
            var slot = slotOpt.get();
            var updatedSlot = com.example.scheduler.domain.model.InterviewSlot.builder()
                .id(slot.getId())
                .interviewerId(slot.getInterviewerId())
                .startTs(slot.getStartTs())
                .endTs(slot.getEndTs())
                .capacity(slot.getCapacity())
                .status(SlotStatus.CLOSED)
                .version(slot.getVersion())
                .build();
            slotPort.update(updatedSlot);
        }
        return "redirect:/ui/interviewer/dashboard";
    }

    @PostMapping("/interviewer/{interviewerId}/slot/{slotId}/delete")
    public String deleteSlot(@PathVariable Long interviewerId,
                             @PathVariable Long slotId,
                             Model model) {
        slotPort.deleteById(slotId);
        return "redirect:/ui/interviewer/dashboard";
    }

    @GetMapping("/student/dashboard")
    public String studentDashboard(HttpSession session,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                   Model model) {
        Object id = session.getAttribute("userId");
        model.addAttribute("candidateId", id);
        boolean candidateExists = false;
        if (id != null) {
            try {
                Long cid = (id instanceof Long) ? (Long) id : Long.valueOf(id.toString());
                candidateExists = candidateService.findById(cid).isPresent();
            } catch (Exception ignored) { }
        }
        model.addAttribute("candidateExists", candidateExists);
        LocalDateTime from;
        LocalDateTime to;
        if (startDate != null || endDate != null) {
            // allow single-sided ranges: if only startDate provided, treat as that single day; if only endDate provided, treat as that single day
            if (startDate == null) startDate = endDate;
            if (endDate == null) endDate = startDate;
            from = startDate.atStartOfDay();
            to = endDate.atTime(LocalTime.MAX);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
        } else {
            // Default show next 7 days
            from = LocalDateTime.now();
            to = from.plusDays(7);
        }
        var page = slotQueryService.listOpenSlots(from, to, null, null, 50);
        model.addAttribute("slots", page.getItems());
        return "student_dashboard";
    }

    @PostMapping("/student/book/{slotId}")
    public String bookSlot(@PathVariable Long slotId,
                           @RequestParam Long candidateId,
                           Model model) {
        try {
            var resp = bookingService.bookSlot(slotId, candidateId);
            model.addAttribute("booking", resp);
            model.addAttribute("message", "Booked slot " + slotId);
        } catch (Exception ex) {
            // Show friendly message on student dashboard instead of JSON error
            model.addAttribute("error", ex.getMessage());
        }
        // After booking attempt, show student dashboard with updated slots
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = from.plusDays(7);
        var page = slotQueryService.listOpenSlots(from, to, null, null, 50);
        model.addAttribute("slots", page.getItems());
        model.addAttribute("candidateId", candidateId);
        return "student_dashboard";
    }

    @GetMapping
    public String uiIndex() {
        return "index"; // reuse existing index which already lists and books slots
    }
}
