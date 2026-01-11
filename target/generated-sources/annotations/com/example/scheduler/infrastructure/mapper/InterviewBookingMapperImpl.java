package com.example.scheduler.infrastructure.mapper;

import com.example.scheduler.domain.model.InterviewBooking;
import com.example.scheduler.infrastructure.persistence.entity.CandidateEntity;
import com.example.scheduler.infrastructure.persistence.entity.InterviewBookingEntity;
import com.example.scheduler.infrastructure.persistence.entity.InterviewSlotEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-11T00:54:10+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Microsoft)"
)
@Component
public class InterviewBookingMapperImpl implements InterviewBookingMapper {

    @Override
    public InterviewBooking toDomain(InterviewBookingEntity entity) {
        if ( entity == null ) {
            return null;
        }

        InterviewBooking.InterviewBookingBuilder interviewBooking = InterviewBooking.builder();

        interviewBooking.candidateId( entityCandidateId( entity ) );
        interviewBooking.slotId( entitySlotId( entity ) );
        interviewBooking.id( entity.getId() );
        interviewBooking.status( entity.getStatus() );
        interviewBooking.bookedAt( entity.getBookedAt() );
        interviewBooking.version( entity.getVersion() );

        return interviewBooking.build();
    }

    @Override
    public InterviewBookingEntity toEntity(InterviewBooking domain) {
        if ( domain == null ) {
            return null;
        }

        InterviewBookingEntity interviewBookingEntity = new InterviewBookingEntity();

        interviewBookingEntity.setStatus( domain.getStatus() );
        interviewBookingEntity.setBookedAt( domain.getBookedAt() );
        interviewBookingEntity.setVersion( domain.getVersion() );

        return interviewBookingEntity;
    }

    private Long entityCandidateId(InterviewBookingEntity interviewBookingEntity) {
        if ( interviewBookingEntity == null ) {
            return null;
        }
        CandidateEntity candidate = interviewBookingEntity.getCandidate();
        if ( candidate == null ) {
            return null;
        }
        Long id = candidate.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entitySlotId(InterviewBookingEntity interviewBookingEntity) {
        if ( interviewBookingEntity == null ) {
            return null;
        }
        InterviewSlotEntity slot = interviewBookingEntity.getSlot();
        if ( slot == null ) {
            return null;
        }
        Long id = slot.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
