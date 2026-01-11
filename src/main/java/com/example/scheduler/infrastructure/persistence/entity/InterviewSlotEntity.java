package com.example.scheduler.infrastructure.persistence.entity;

import com.example.scheduler.domain.model.SlotStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "interview_slot",
        uniqueConstraints = @UniqueConstraint(name = "uniq_slot", columnNames = {"interviewer_id", "start_ts", "end_ts"}))
@Getter
@Setter
@NoArgsConstructor
public class InterviewSlotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interviewer_id", nullable = false)
    private InterviewerEntity interviewer;

    @Column(name = "start_ts", nullable = false)
    private LocalDateTime startTs;

    @Column(name = "end_ts", nullable = false)
    private LocalDateTime endTs;

    @Column(nullable = false)
    private int capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotStatus status;

    @Version
    private int version;
}

