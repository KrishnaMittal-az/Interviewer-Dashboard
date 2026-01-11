package com.example.scheduler.infrastructure.persistence.jpa;

import com.example.scheduler.domain.model.BookingStatus;
import com.example.scheduler.infrastructure.persistence.entity.InterviewBookingEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface InterviewBookingJpaRepository extends JpaRepository<InterviewBookingEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from InterviewBookingEntity b where b.id = :id")
    Optional<InterviewBookingEntity> findForUpdate(@Param("id") Long id);

    boolean existsByCandidateIdAndStatus(Long candidateId, BookingStatus status);

    @Query("""
            select count(b) from InterviewBookingEntity b
            where b.slot.interviewer.id = :interviewerId
              and b.status = 'BOOKED'
              and b.slot.startTs between :start and :end
            """)
    long countActiveBookingsForWeek(@Param("interviewerId") Long interviewerId,
                                    @Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end);

        @Query("""
          select b from InterviewBookingEntity b
          where b.slot.interviewer.id = :interviewerId
          order by b.bookedAt desc
          """)
        java.util.List<InterviewBookingEntity> findByInterviewerId(@Param("interviewerId") Long interviewerId);
}

