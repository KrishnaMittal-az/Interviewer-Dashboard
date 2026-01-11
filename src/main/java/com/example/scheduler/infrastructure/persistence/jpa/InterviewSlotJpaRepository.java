package com.example.scheduler.infrastructure.persistence.jpa;

import com.example.scheduler.domain.model.SlotStatus;
import com.example.scheduler.infrastructure.persistence.entity.InterviewSlotEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InterviewSlotJpaRepository extends JpaRepository<InterviewSlotEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from InterviewSlotEntity s where s.id = :id")
    Optional<InterviewSlotEntity> findForUpdate(@Param("id") Long id);

    boolean existsByInterviewerIdAndStartTsAndEndTs(Long interviewerId, LocalDateTime start, LocalDateTime end);

    @Query("""
            select s from InterviewSlotEntity s
            where s.status = :status
              and s.startTs between :from and :to
              and (:interviewerId is null or s.interviewer.id = :interviewerId)
              and ((s.startTs > :cursorTs) or (s.startTs = :cursorTs and s.id > :cursorId))
            order by s.startTs asc, s.id asc
            """)
    List<InterviewSlotEntity> findPage(@Param("status") SlotStatus status,
                                       @Param("from") LocalDateTime from,
                                       @Param("to") LocalDateTime to,
                                       @Param("interviewerId") Long interviewerId,
                                       @Param("cursorTs") LocalDateTime cursorTs,
                                       @Param("cursorId") Long cursorId,
                                       Pageable pageable);

    void deleteByStatusAndInterviewerIdAndStartTsBetween(SlotStatus status, Long interviewerId, LocalDateTime from, LocalDateTime to);
}

