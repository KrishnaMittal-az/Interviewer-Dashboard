package com.example.scheduler.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateInterviewerRequest {
    @NotBlank
    private String name;
    @Min(1)
    private int weeklyCapacity;
}

