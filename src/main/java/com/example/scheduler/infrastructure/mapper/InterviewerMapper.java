package com.example.scheduler.infrastructure.mapper;

import com.example.scheduler.domain.model.Interviewer;
import com.example.scheduler.infrastructure.persistence.entity.InterviewerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InterviewerMapper {
    InterviewerMapper INSTANCE = Mappers.getMapper(InterviewerMapper.class);

    Interviewer toDomain(InterviewerEntity entity);

    @Mapping(target = "id", ignore = true)
    InterviewerEntity toEntity(Interviewer domain);
}

