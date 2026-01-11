package com.example.scheduler.infrastructure.adapter;

import com.example.scheduler.domain.model.Interviewer;
import com.example.scheduler.domain.repository.InterviewerPort;
import com.example.scheduler.infrastructure.mapper.InterviewerMapper;
import com.example.scheduler.infrastructure.persistence.entity.InterviewerEntity;
import com.example.scheduler.infrastructure.persistence.jpa.InterviewerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InterviewerAdapter implements InterviewerPort {

    private final InterviewerJpaRepository repository;
    private final InterviewerMapper mapper;

    @Override
    public Interviewer save(Interviewer interviewer) {
        InterviewerEntity entity = mapper.toEntity(interviewer);
        if (interviewer.getId() != null) {
            entity.setId(interviewer.getId());
        }
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<Interviewer> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }
}

