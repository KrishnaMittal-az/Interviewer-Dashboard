package com.example.scheduler.infrastructure.mapper;

import com.example.scheduler.domain.model.WeeklyAvailability;
import com.example.scheduler.infrastructure.persistence.entity.InterviewerEntity;
import com.example.scheduler.infrastructure.persistence.entity.WeeklyAvailabilityEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-12T00:18:58+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260101-2150, environment: Java 21.0.9 (Eclipse Adoptium)"
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
        weeklyAvailability.dayOfWeek( entity.getDayOfWeek() );
        weeklyAvailability.endTime( entity.getEndTime() );
        weeklyAvailability.id( entity.getId() );
        weeklyAvailability.slotDurationMinutes( entity.getSlotDurationMinutes() );
        weeklyAvailability.startTime( entity.getStartTime() );

        return weeklyAvailability.build();
    }

    @Override
    public WeeklyAvailabilityEntity toEntity(WeeklyAvailability domain) {
        if ( domain == null ) {
            return null;
        }

        WeeklyAvailabilityEntity weeklyAvailabilityEntity = new WeeklyAvailabilityEntity();

        weeklyAvailabilityEntity.setDayOfWeek( domain.getDayOfWeek() );
        weeklyAvailabilityEntity.setEndTime( domain.getEndTime() );
        weeklyAvailabilityEntity.setSlotDurationMinutes( domain.getSlotDurationMinutes() );
        weeklyAvailabilityEntity.setStartTime( domain.getStartTime() );

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
