package com.pl.bas.service;

import com.pl.bas.domain.Producer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProducerService {

    Producer save(Producer producer);

    Page<Producer> findAll(Pageable pageable);

    Optional<Producer> findOne(Long id);

    void delete(Long id);
}
