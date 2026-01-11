package com.example.scheduler.infrastructure.mapper;

import com.example.scheduler.domain.model.WeeklyAvailability;
import com.example.scheduler.infrastructure.persistence.entity.InterviewerEntity;
import com.example.scheduler.infrastructure.persistence.entity.WeeklyAvailabilityEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-11T00:54:10+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Microsoft)"
)
@Component
public class WeeklyAvailabilityMapperImpl implements WeeklyAvailabilityMapper {

    @Override
    public WeeklyAvailability toDomain(WeeklyAvailabilityEntity entity) {
        if ( entity == null ) {
            return null;
        }

        WeeklyAvailability.WeeklyAvailabilityBuilder weeklyAvailability = WeeklyAvailability.builder();

        weeklyAvailability.interviewerId( entityInterviewerId( entity ) );
        weeklyAvailability.id( entity.getId() );
        weeklyAvailability.dayOfWeek( entity.getDayOfWeek() );
        weeklyAvailability.startTime( entity.getStartTime() );
        weeklyAvailability.endTime( entity.getEndTime() );
        weeklyAvailability.slotDurationMinutes( entity.getSlotDurationMinutes() );

        return weeklyAvailability.build();
    }

    @Override
    public WeeklyAvailabilityEntity toEntity(WeeklyAvailability domain) {
        if ( domain == null ) {
            return null;
        }

        WeeklyAvailabilityEntity weeklyAvailabilityEntity = new WeeklyAvailabilityEntity();

        weeklyAvailabilityEntity.setDayOfWeek( domain.getDayOfWeek() );
        weeklyAvailabilityEntity.setStartTime( domain.getStartTime() );
        weeklyAvailabilityEntity.setEndTime( domain.getEndTime() );
        weeklyAvailabilityEntity.setSlotDurationMinutes( domain.getSlotDurationMinutes() );

        return weeklyAvailabilityEntity;
    }

    private Long entityInterviewerId(WeeklyAvailabilityEntity weeklyAvailabilityEntity) {
        if ( weeklyAvailabilityEntity == null ) {
            return null;
        }
        InterviewerEntity interviewer = weeklyAvailabilityEntity.getInterviewer();
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
