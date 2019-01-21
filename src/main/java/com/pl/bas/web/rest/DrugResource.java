package com.pl.bas.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pl.bas.domain.Drug;
import com.pl.bas.security.SecurityUtils;
import com.pl.bas.service.DrugService;
import com.pl.bas.service.UserService;
import com.pl.bas.web.rest.errors.BadRequestAlertException;
import com.pl.bas.web.rest.util.HeaderUtil;
import com.pl.bas.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class DrugResource {

    private final Logger log = LoggerFactory.getLogger(DrugResource.class);

    private static final String ENTITY_NAME = "drug";

    private final DrugService drugService;
    private UserService userService;

    public DrugResource(DrugService drugService) {
        this.drugService = drugService;
    }

    @Autowired
    public DrugResource(DrugService drugService, UserService userService) {
        this.drugService = drugService;
        this.userService = userService;
    }

    @PostMapping("/drugs")
    @Timed
    public ResponseEntity<Drug> createDrug(@Valid @RequestBody Drug drug) throws URISyntaxException {
        log.debug("REST request to save Drug : {}", drug);
        if (drug.getId() != null) {
            throw new BadRequestAlertException("A new drug cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Drug result = drugService.save(drug);
        return ResponseEntity.created(new URI("/api/drugs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/drugs")
    @Timed
    public ResponseEntity<Drug> updateDrug(@Valid @RequestBody Drug drug) throws URISyntaxException {
        log.debug("REST request to update Drug : {}", drug);
        if (drug.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Drug result = drugService.save(drug);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, drug.getId().toString()))
            .body(result);
    }

    @GetMapping("/drugs")
    @Timed
    public ResponseEntity<List<Drug>> getAllDrugs(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Drugs");
        eagerload = true;
        Page<Drug> page;
        if (eagerload) {
            page = drugService.findAllWithEagerRelationships(pageable);
        } else {
            page = drugService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/drugs?eagerload=%b", eagerload));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/drugs/favourite-drugs")
    public ResponseEntity<List<Drug>> getFavouriteDrugs() {
        Long id = userService.findByLogin(SecurityUtils.getCurrentUserLogin().get()).getId();
        return ResponseEntity.ok().body(drugService.findAll()
            .stream()
            .filter(drug -> drug.getUsersWithFavourite().stream().anyMatch(user -> user.getId().equals(id)))
            .collect(Collectors.toList()));
    }

    @GetMapping("/drugs/{id}")
    @Timed
    public ResponseEntity<Drug> getDrug(@PathVariable Long id) {
        log.debug("REST request to get Drug : {}", id);
        Optional<Drug> drug = drugService.findOne(id);
        return ResponseUtil.wrapOrNotFound(drug);
    }

    @DeleteMapping("/drugs/{id}")
    @Timed
    public ResponseEntity<Void> deleteDrug(@PathVariable Long id) {
        log.debug("REST request to delete Drug : {}", id);
        drugService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
