package com.pl.bas.service;

import com.pl.bas.domain.Drug;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DrugService {

    Drug save(Drug drug);

    Page<Drug> findAll(Pageable pageable);

    Page<Drug> findAllWithEagerRelationships(Pageable pageable);
    
    Optional<Drug> findOne(Long id);

    void delete(Long id);

    List<Drug> findAll();
}
