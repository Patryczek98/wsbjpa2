package com.jpacourse.service.impl;

import com.jpacourse.persistance.entity.VisitEntity;
import com.jpacourse.persistance.repository.VisitRepository;
import com.jpacourse.service.VisitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;

    public VisitServiceImpl(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    @Override
    @Transactional
    public VisitEntity saveVisit(VisitEntity visit) {
        // Upewnij się, że relacja patient jest ustawiona obustronnie
        if (visit.getPatient() != null) {
            visit.getPatient().getVisits().add(visit);
        }
        return visitRepository.save(visit);
    }

    @Override
    public Optional<VisitEntity> findVisitById(Long id) {
        return visitRepository.findById(id);
    }
}