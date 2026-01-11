package com.example.scheduler.infrastructure.mapper;

import com.example.scheduler.domain.model.Candidate;
import com.example.scheduler.infrastructure.persistence.entity.CandidateEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-11T23:42:27+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260101-2150, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class CandidateMapperImpl implements CandidateMapper {

    @Override
    public Candidate toDomain(CandidateEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Candidate.CandidateBuilder candidate = Candidate.builder();

        candidate.email( entity.getEmail() );
        candidate.id( entity.getId() );
        candidate.name( entity.getName() );

        return candidate.build();
    }

    @Override
    public CandidateEntity toEntity(Candidate domain) {
        if ( domain == null ) {
            return null;
        }

        CandidateEntity candidateEntity = new CandidateEntity();

        candidateEntity.setEmail( domain.getEmail() );
        candidateEntity.setName( domain.getName() );

        return candidateEntity;
    }
}
