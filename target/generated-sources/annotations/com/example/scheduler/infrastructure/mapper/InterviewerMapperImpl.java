package com.example.scheduler.infrastructure.mapper;

import com.example.scheduler.domain.model.Interviewer;
import com.example.scheduler.infrastructure.persistence.entity.InterviewerEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-11T23:42:27+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260101-2150, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class InterviewerMapperImpl implements InterviewerMapper {

    @Override
    public Interviewer toDomain(InterviewerEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Interviewer.InterviewerBuilder interviewer = Interviewer.builder();

        interviewer.id( entity.getId() );
        interviewer.name( entity.getName() );
        interviewer.weeklyCapacity( entity.getWeeklyCapacity() );

        return interviewer.build();
    }

    @Override
    public InterviewerEntity toEntity(Interviewer domain) {
        if ( domain == null ) {
            return null;
        }

        InterviewerEntity interviewerEntity = new InterviewerEntity();

        interviewerEntity.setName( domain.getName() );
        interviewerEntity.setWeeklyCapacity( domain.getWeeklyCapacity() );

        return interviewerEntity;
    }
}
