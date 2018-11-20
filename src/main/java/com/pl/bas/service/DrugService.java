package com.pl.bas.service;

import com.pl.bas.domain.Drug;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Drug.
 */
public interface DrugService {

    /**
     * Save a drug.
     *
     * @param drug the entity to save
     * @return the persisted entity
     */
    Drug save(Drug drug);

    /**
     * Get all the drugs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Drug> findAll(Pageable pageable);

    /**
     * Get all the Drug with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<Drug> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" drug.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Drug> findOne(Long id);

    /**
     * Delete the "id" drug.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    List<Drug> findAll();
}
