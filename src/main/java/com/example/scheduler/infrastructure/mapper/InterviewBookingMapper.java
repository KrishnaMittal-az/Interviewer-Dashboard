package com.example.scheduler.infrastructure.mapper;

import com.example.scheduler.domain.model.InterviewBooking;
import com.example.scheduler.infrastructure.persistence.entity.InterviewBookingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InterviewBookingMapper {
    InterviewBookingMapper INSTANCE = Mappers.getMapper(InterviewBookingMapper.class);

    @Mapping(source = "candidate.id", target = "candidateId")
    @Mapping(source = "slot.id", target = "slotId")
    InterviewBooking toDomain(InterviewBookingEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "candidate", ignore = true)
    @Mapping(target = "slot", ignore = true)
    InterviewBookingEntity toEntity(InterviewBooking domain);
}

