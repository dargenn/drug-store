package com.pl.bas.service.impl;

import com.pl.bas.service.ProducerService;
import com.pl.bas.domain.Producer;
import com.pl.bas.repository.ProducerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ProducerServiceImpl implements ProducerService {

    private final Logger log = LoggerFactory.getLogger(ProducerServiceImpl.class);

    private final ProducerRepository producerRepository;

    public ProducerServiceImpl(ProducerRepository producerRepository) {
        this.producerRepository = producerRepository;
    }

    @Override
    public Producer save(Producer producer) {
        log.debug("Request to save Producer : {}", producer);
        return producerRepository.save(producer);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Producer> findAll(Pageable pageable) {
        log.debug("Request to get all Producers");
        return producerRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Producer> findOne(Long id) {
        log.debug("Request to get Producer : {}", id);
        return producerRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Producer : {}", id);
        producerRepository.deleteById(id);
    }
}
