package com.example.scheduler.infrastructure.adapter;

import com.example.scheduler.domain.model.WeeklyAvailability;
import com.example.scheduler.domain.repository.WeeklyAvailabilityPort;
import com.example.scheduler.infrastructure.mapper.WeeklyAvailabilityMapper;
import com.example.scheduler.infrastructure.persistence.entity.WeeklyAvailabilityEntity;
import com.example.scheduler.infrastructure.persistence.jpa.WeeklyAvailabilityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WeeklyAvailabilityAdapter implements WeeklyAvailabilityPort {

    private final WeeklyAvailabilityJpaRepository repository;
    private final WeeklyAvailabilityMapper mapper;

    @Override
    public WeeklyAvailability save(WeeklyAvailability availability) {
        WeeklyAvailabilityEntity entity = mapper.toEntity(availability);
        if (availability.getId() != null) {
            entity.setId(availability.getId());
        }
        // attach interviewer reference by id only to avoid extra load
        var interviewerRef = new com.example.scheduler.infrastructure.persistence.entity.InterviewerEntity();
        interviewerRef.setId(availability.getInterviewerId());
        entity.setInterviewer(interviewerRef);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public void deleteByInterviewerId(Long interviewerId) {
        repository.deleteByInterviewerId(interviewerId);
    }

    @Override
    public List<WeeklyAvailability> findByInterviewerId(Long interviewerId) {
        return repository.findByInterviewerId(interviewerId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public java.util.Optional<WeeklyAvailability> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

