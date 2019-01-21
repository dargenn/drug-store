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

@RestController
@RequestMapping("/api")
public class ProducerResource {

    private final Logger log = LoggerFactory.getLogger(ProducerResource.class);

    private static final String ENTITY_NAME = "producer";

    private final ProducerService producerService;

    public ProducerResource(ProducerService producerService) {
        this.producerService = producerService;
    }

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

    @GetMapping("/producers")
    @Timed
    public ResponseEntity<List<Producer>> getAllProducers(Pageable pageable) {
        log.debug("REST request to get a page of Producers");
        Page<Producer> page = producerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/producers");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/producers/{id}")
    @Timed
    public ResponseEntity<Producer> getProducer(@PathVariable Long id) {
        log.debug("REST request to get Producer : {}", id);
        Optional<Producer> producer = producerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(producer);
    }

    @DeleteMapping("/producers/{id}")
    @Timed
    public ResponseEntity<Void> deleteProducer(@PathVariable Long id) {
        log.debug("REST request to delete Producer : {}", id);
        producerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
