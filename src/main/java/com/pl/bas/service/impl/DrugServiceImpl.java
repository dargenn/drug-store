package com.pl.bas.service.impl;

import com.pl.bas.service.DrugService;
import com.pl.bas.domain.Drug;
import com.pl.bas.repository.DrugRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Drug.
 */
@Service
@Transactional
public class DrugServiceImpl implements DrugService {

    private final Logger log = LoggerFactory.getLogger(DrugServiceImpl.class);

    private final DrugRepository drugRepository;

    public DrugServiceImpl(DrugRepository drugRepository) {
        this.drugRepository = drugRepository;
    }

    /**
     * Save a drug.
     *
     * @param drug the entity to save
     * @return the persisted entity
     */
    @Override
    public Drug save(Drug drug) {
        log.debug("Request to save Drug : {}", drug);
        return drugRepository.save(drug);
    }

    /**
     * Get all the drugs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Drug> findAll(Pageable pageable) {
        log.debug("Request to get all Drugs");
        return drugRepository.findAll(pageable);
    }

    /**
     * Get all the Drug with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<Drug> findAllWithEagerRelationships(Pageable pageable) {
        return drugRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one drug by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Drug> findOne(Long id) {
        log.debug("Request to get Drug : {}", id);
        return drugRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the drug by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Drug : {}", id);
        drugRepository.deleteById(id);
    }

    @Override
    public List<Drug> findAll() {
        return drugRepository.findAll();
    }
}
