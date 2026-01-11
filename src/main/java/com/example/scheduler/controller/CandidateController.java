package com.example.scheduler.controller;

import com.example.scheduler.domain.model.Candidate;
import com.example.scheduler.dto.CandidateRequest;
import com.example.scheduler.service.CandidateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Candidate create(@RequestBody @Valid CandidateRequest request) {
        return candidateService.create(request);
    }
}

