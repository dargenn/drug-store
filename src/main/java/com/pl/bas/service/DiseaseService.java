package com.pl.bas.service;

import com.pl.bas.domain.Disease;

import java.util.List;
import java.util.Optional;

public interface DiseaseService {

    Disease save(Disease disease);

    List<Disease> findAll();

    Optional<Disease> findOne(Long id);

    void delete(Long id);
}
