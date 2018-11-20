package com.pl.bas.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pl.bas.domain.Producer;
import com.pl.bas.service.ProducerService;
import com.pl.bas.web.rest.errors.BadRequestAlertException;
import com.pl.bas.web.rest.util.HeaderUtil;
import com.pl.bas.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Producer.
 */
@RestController
@RequestMapping("/api")
public class ProducerResource {

    private final Logger log = LoggerFactory.getLogger(ProducerResource.class);

    private static final String ENTITY_NAME = "producer";

    private final ProducerService producerService;

    public ProducerResource(ProducerService producerService) {
        this.producerService = producerService;
    }

    /**
     * POST  /producers : Create a new producer.
     *
     * @param producer the producer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new producer, or with status 400 (Bad Request) if the producer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/producers")
    @Timed
    public ResponseEntity<Producer> createProducer(@Valid @RequestBody Producer producer) throws URISyntaxException {
        log.debug("REST request to save Producer : {}", producer);
        if (producer.getId() != null) {
            throw new BadRequestAlertException("A new producer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Producer result = producerService.save(producer);
        return ResponseEntity.created(new URI("/api/producers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /producers : Updates an existing producer.
     *
     * @param producer the producer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated producer,
     * or with status 400 (Bad Request) if the producer is not valid,
     * or with status 500 (Internal Server Error) if the producer couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/producers")
    @Timed
    public ResponseEntity<Producer> updateProducer(@Valid @RequestBody Producer producer) throws URISyntaxException {
        log.debug("REST request to update Producer : {}", producer);
        if (producer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Producer result = producerService.save(producer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, producer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /producers : get all the producers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of producers in body
     */
    @GetMapping("/producers")
    @Timed
    public ResponseEntity<List<Producer>> getAllProducers(Pageable pageable) {
        log.debug("REST request to get a page of Producers");
        Page<Producer> page = producerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/producers");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /producers/:id : get the "id" producer.
     *
     * @param id the id of the producer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the producer, or with status 404 (Not Found)
     */
    @GetMapping("/producers/{id}")
    @Timed
    public ResponseEntity<Producer> getProducer(@PathVariable Long id) {
        log.debug("REST request to get Producer : {}", id);
        Optional<Producer> producer = producerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(producer);
    }

    /**
     * DELETE  /producers/:id : delete the "id" producer.
     *
     * @param id the id of the producer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/producers/{id}")
    @Timed
    public ResponseEntity<Void> deleteProducer(@PathVariable Long id) {
        log.debug("REST request to delete Producer : {}", id);
        producerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
