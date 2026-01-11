package com.example.scheduler.infrastructure.persistence.entity;

import com.example.scheduler.domain.model.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "interview_booking",
        uniqueConstraints = @UniqueConstraint(name = "uniq_candidate_slot", columnNames = {"candidate_id", "slot_id"}))
@Getter
@Setter
@NoArgsConstructor
public class InterviewBookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateEntity candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", nullable = false)
    private InterviewSlotEntity slot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.BOOKED;

    @Column(name = "booked_at", nullable = false)
    private LocalDateTime bookedAt;

    @Version
    private int version;
}

