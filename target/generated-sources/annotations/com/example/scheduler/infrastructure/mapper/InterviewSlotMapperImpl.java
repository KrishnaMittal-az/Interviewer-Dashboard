package com.example.scheduler.infrastructure.mapper;

import com.example.scheduler.domain.model.InterviewSlot;
import com.example.scheduler.infrastructure.persistence.entity.InterviewSlotEntity;
import com.example.scheduler.infrastructure.persistence.entity.InterviewerEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-11T00:54:10+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Microsoft)"
)
@Component
public class InterviewSlotMapperImpl implements InterviewSlotMapper {

    @Override
    public InterviewSlot toDomain(InterviewSlotEntity entity) {
        if ( entity == null ) {
            return null;
        }

        InterviewSlot.InterviewSlotBuilder interviewSlot = InterviewSlot.builder();

        interviewSlot.interviewerId( entityInterviewerId( entity ) );
        interviewSlot.id( entity.getId() );
        interviewSlot.startTs( entity.getStartTs() );
        interviewSlot.endTs( entity.getEndTs() );
        interviewSlot.capacity( entity.getCapacity() );
        interviewSlot.status( entity.getStatus() );
        interviewSlot.version( entity.getVersion() );

        return interviewSlot.build();
    }

    @Override
    public InterviewSlotEntity toEntity(InterviewSlot domain) {
        if ( domain == null ) {
            return null;
        }

        InterviewSlotEntity interviewSlotEntity = new InterviewSlotEntity();

        interviewSlotEntity.setStartTs( domain.getStartTs() );
        interviewSlotEntity.setEndTs( domain.getEndTs() );
        interviewSlotEntity.setCapacity( domain.getCapacity() );
        interviewSlotEntity.setStatus( domain.getStatus() );
        interviewSlotEntity.setVersion( domain.getVersion() );

        return interviewSlotEntity;
    }

    private Long entityInterviewerId(InterviewSlotEntity interviewSlotEntity) {
        if ( interviewSlotEntity == null ) {
            return null;
        }
        InterviewerEntity interviewer = interviewSlotEntity.getInterviewer();
        if ( interviewer == null ) {
            return null;
        }
        Long id = interviewer.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
