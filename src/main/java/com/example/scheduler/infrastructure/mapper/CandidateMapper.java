package com.example.scheduler.infrastructure.mapper;

import com.example.scheduler.domain.model.Candidate;
import com.example.scheduler.infrastructure.persistence.entity.CandidateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CandidateMapper {
    CandidateMapper INSTANCE = Mappers.getMapper(CandidateMapper.class);

    Candidate toDomain(CandidateEntity entity);

    @Mapping(target = "id", ignore = true)
    CandidateEntity toEntity(Candidate domain);
}

