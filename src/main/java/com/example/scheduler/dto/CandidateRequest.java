package com.example.scheduler.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CandidateRequest {
    @NotBlank
    private String name;
    @Email
    private String email;
}

