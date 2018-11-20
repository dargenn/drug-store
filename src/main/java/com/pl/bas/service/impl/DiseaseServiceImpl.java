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

/**
 * Service Implementation for managing Disease.
 */
@Service
@Transactional
public class DiseaseServiceImpl implements DiseaseService {

    private final Logger log = LoggerFactory.getLogger(DiseaseServiceImpl.class);

    private final DiseaseRepository diseaseRepository;

    public DiseaseServiceImpl(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    /**
     * Save a disease.
     *
     * @param disease the entity to save
     * @return the persisted entity
     */
    @Override
    public Disease save(Disease disease) {
        log.debug("Request to save Disease : {}", disease);
        return diseaseRepository.save(disease);
    }

    /**
     * Get all the diseases.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Disease> findAll() {
        log.debug("Request to get all Diseases");
        return diseaseRepository.findAll();
    }


    /**
     * Get one disease by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Disease> findOne(Long id) {
        log.debug("Request to get Disease : {}", id);
        return diseaseRepository.findById(id);
    }

    /**
     * Delete the disease by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Disease : {}", id);
        diseaseRepository.deleteById(id);
    }
}
