package com.example.scheduler.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookSlotRequest {
    @NotNull
    private Long candidateId;
}

