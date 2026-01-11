package com.example.scheduler.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Candidate {
    Long id;
    String name;
    String email;
}

