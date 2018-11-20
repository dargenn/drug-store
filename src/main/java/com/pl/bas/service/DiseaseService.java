package com.pl.bas.service;

import com.pl.bas.domain.Disease;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Disease.
 */
public interface DiseaseService {

    /**
     * Save a disease.
     *
     * @param disease the entity to save
     * @return the persisted entity
     */
    Disease save(Disease disease);

    /**
     * Get all the diseases.
     *
     * @return the list of entities
     */
    List<Disease> findAll();


    /**
     * Get the "id" disease.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Disease> findOne(Long id);

    /**
     * Delete the "id" disease.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
