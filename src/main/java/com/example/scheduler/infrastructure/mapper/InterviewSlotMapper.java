package com.example.scheduler.infrastructure.mapper;

import com.example.scheduler.domain.model.InterviewSlot;
import com.example.scheduler.infrastructure.persistence.entity.InterviewSlotEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InterviewSlotMapper {
    InterviewSlotMapper INSTANCE = Mappers.getMapper(InterviewSlotMapper.class);

    @Mapping(source = "interviewer.id", target = "interviewerId")
    InterviewSlot toDomain(InterviewSlotEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "interviewer", ignore = true)
    InterviewSlotEntity toEntity(InterviewSlot domain);
}

