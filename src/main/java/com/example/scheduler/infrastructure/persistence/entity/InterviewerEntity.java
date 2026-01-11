package com.example.scheduler.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "interviewer")
@Getter
@Setter
@NoArgsConstructor
public class InterviewerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "weekly_capacity")
    private int weeklyCapacity;
}

