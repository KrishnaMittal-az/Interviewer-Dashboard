package com.example.scheduler.infrastructure.mapper;

import com.example.scheduler.domain.model.WeeklyAvailability;
import com.example.scheduler.infrastructure.persistence.entity.WeeklyAvailabilityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WeeklyAvailabilityMapper {
    WeeklyAvailabilityMapper INSTANCE = Mappers.getMapper(WeeklyAvailabilityMapper.class);

    @Mapping(source = "interviewer.id", target = "interviewerId")
    WeeklyAvailability toDomain(WeeklyAvailabilityEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "interviewer", ignore = true)
    WeeklyAvailabilityEntity toEntity(WeeklyAvailability domain);
}

