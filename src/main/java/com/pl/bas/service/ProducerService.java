package com.pl.bas.service;

import com.pl.bas.domain.Producer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Producer.
 */
public interface ProducerService {

    /**
     * Save a producer.
     *
     * @param producer the entity to save
     * @return the persisted entity
     */
    Producer save(Producer producer);

    /**
     * Get all the producers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Producer> findAll(Pageable pageable);


    /**
     * Get the "id" producer.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Producer> findOne(Long id);

    /**
     * Delete the "id" producer.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
