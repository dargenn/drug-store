package com.pl.bas.web.rest;

import com.pl.bas.DrugstoreApp;

import com.pl.bas.domain.Producer;
import com.pl.bas.repository.ProducerRepository;
import com.pl.bas.service.ProducerService;
import com.pl.bas.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.pl.bas.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProducerResource REST controller.
 *
 * @see ProducerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DrugstoreApp.class)
public class ProducerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private ProducerService producerService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProducerMockMvc;

    private Producer producer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProducerResource producerResource = new ProducerResource(producerService);
        this.restProducerMockMvc = MockMvcBuilders.standaloneSetup(producerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Producer createEntity(EntityManager em) {
        Producer producer = new Producer()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return producer;
    }

    @Before
    public void initTest() {
        producer = createEntity(em);
    }

    @Test
    @Transactional
    public void createProducer() throws Exception {
        int databaseSizeBeforeCreate = producerRepository.findAll().size();

        // Create the Producer
        restProducerMockMvc.perform(post("/api/producers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(producer)))
            .andExpect(status().isCreated());

        // Validate the Producer in the database
        List<Producer> producerList = producerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeCreate + 1);
        Producer testProducer = producerList.get(producerList.size() - 1);
        assertThat(testProducer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProducer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createProducerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = producerRepository.findAll().size();

        // Create the Producer with an existing ID
        producer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProducerMockMvc.perform(post("/api/producers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(producer)))
            .andExpect(status().isBadRequest());

        // Validate the Producer in the database
        List<Producer> producerList = producerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = producerRepository.findAll().size();
        // set the field null
        producer.setName(null);

        // Create the Producer, which fails.

        restProducerMockMvc.perform(post("/api/producers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(producer)))
            .andExpect(status().isBadRequest());

        List<Producer> producerList = producerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = producerRepository.findAll().size();
        // set the field null
        producer.setDescription(null);

        // Create the Producer, which fails.

        restProducerMockMvc.perform(post("/api/producers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(producer)))
            .andExpect(status().isBadRequest());

        List<Producer> producerList = producerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProducers() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        // Get all the producerList
        restProducerMockMvc.perform(get("/api/producers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(producer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getProducer() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        // Get the producer
        restProducerMockMvc.perform(get("/api/producers/{id}", producer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(producer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProducer() throws Exception {
        // Get the producer
        restProducerMockMvc.perform(get("/api/producers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProducer() throws Exception {
        // Initialize the database
        producerService.save(producer);

        int databaseSizeBeforeUpdate = producerRepository.findAll().size();

        // Update the producer
        Producer updatedProducer = producerRepository.findById(producer.getId()).get();
        // Disconnect from session so that the updates on updatedProducer are not directly saved in db
        em.detach(updatedProducer);
        updatedProducer
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restProducerMockMvc.perform(put("/api/producers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProducer)))
            .andExpect(status().isOk());

        // Validate the Producer in the database
        List<Producer> producerList = producerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeUpdate);
        Producer testProducer = producerList.get(producerList.size() - 1);
        assertThat(testProducer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProducer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingProducer() throws Exception {
        int databaseSizeBeforeUpdate = producerRepository.findAll().size();

        // Create the Producer

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProducerMockMvc.perform(put("/api/producers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(producer)))
            .andExpect(status().isBadRequest());

        // Validate the Producer in the database
        List<Producer> producerList = producerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProducer() throws Exception {
        // Initialize the database
        producerService.save(producer);

        int databaseSizeBeforeDelete = producerRepository.findAll().size();

        // Get the producer
        restProducerMockMvc.perform(delete("/api/producers/{id}", producer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Producer> producerList = producerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Producer.class);
        Producer producer1 = new Producer();
        producer1.setId(1L);
        Producer producer2 = new Producer();
        producer2.setId(producer1.getId());
        assertThat(producer1).isEqualTo(producer2);
        producer2.setId(2L);
        assertThat(producer1).isNotEqualTo(producer2);
        producer1.setId(null);
        assertThat(producer1).isNotEqualTo(producer2);
    }
}
