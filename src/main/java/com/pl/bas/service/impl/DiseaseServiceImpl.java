package com.pl.bas.service.impl;

import com.pl.bas.service.DiseaseService;
import com.pl.bas.domain.Disease;
import com.pl.bas.repository.DiseaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DiseaseServiceImpl implements DiseaseService {

    private final Logger log = LoggerFactory.getLogger(DiseaseServiceImpl.class);

    private final DiseaseRepository diseaseRepository;

    public DiseaseServiceImpl(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    @Override
    public Disease save(Disease disease) {
        log.debug("Request to save Disease : {}", disease);
        return diseaseRepository.save(disease);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Disease> findAll() {
        log.debug("Request to get all Diseases");
        return diseaseRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Disease> findOne(Long id) {
        log.debug("Request to get Disease : {}", id);
        return diseaseRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Disease : {}", id);
        diseaseRepository.deleteById(id);
    }
}
