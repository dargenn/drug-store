package com.pl.bas.web.rest;

import com.pl.bas.DrugstoreApp;

import com.pl.bas.domain.Drug;
import com.pl.bas.repository.DrugRepository;
import com.pl.bas.service.DrugService;
import com.pl.bas.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import static com.pl.bas.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pl.bas.domain.enumeration.Form;
import com.pl.bas.domain.enumeration.Rating;
/**
 * Test class for the DrugResource REST controller.
 *
 * @see DrugResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DrugstoreApp.class)
public class DrugResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Form DEFAULT_FORM = Form.TABLET;
    private static final Form UPDATED_FORM = Form.SOLUTION;

    private static final String DEFAULT_DOSE = "AAAAAAAAAA";
    private static final String UPDATED_DOSE = "BBBBBBBBBB";

    private static final String DEFAULT_PACKAGING = "AAAAAAAAAA";
    private static final String UPDATED_PACKAGING = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final Rating DEFAULT_RATING = Rating.BAD;
    private static final Rating UPDATED_RATING = Rating.NEUTRAL;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private DrugRepository drugRepository;

    @Mock
    private DrugRepository drugRepositoryMock;

    @Mock
    private DrugService drugServiceMock;

    @Autowired
    private DrugService drugService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDrugMockMvc;

    private Drug drug;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DrugResource drugResource = new DrugResource(drugService);
        this.restDrugMockMvc = MockMvcBuilders.standaloneSetup(drugResource)
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
    public static Drug createEntity(EntityManager em) {
        Drug drug = new Drug()
            .name(DEFAULT_NAME)
            .form(DEFAULT_FORM)
            .dose(DEFAULT_DOSE)
            .packaging(DEFAULT_PACKAGING)
            .price(DEFAULT_PRICE)
            .rating(DEFAULT_RATING)
            .description(DEFAULT_DESCRIPTION);
        return drug;
    }

    @Before
    public void initTest() {
        drug = createEntity(em);
    }

    @Test
    @Transactional
    public void createDrug() throws Exception {
        int databaseSizeBeforeCreate = drugRepository.findAll().size();

        // Create the Drug
        restDrugMockMvc.perform(post("/api/drugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(drug)))
            .andExpect(status().isCreated());

        // Validate the Drug in the database
        List<Drug> drugList = drugRepository.findAll();
        assertThat(drugList).hasSize(databaseSizeBeforeCreate + 1);
        Drug testDrug = drugList.get(drugList.size() - 1);
        assertThat(testDrug.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDrug.getForm()).isEqualTo(DEFAULT_FORM);
        assertThat(testDrug.getDose()).isEqualTo(DEFAULT_DOSE);
        assertThat(testDrug.getPackaging()).isEqualTo(DEFAULT_PACKAGING);
        assertThat(testDrug.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testDrug.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testDrug.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createDrugWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = drugRepository.findAll().size();

        // Create the Drug with an existing ID
        drug.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDrugMockMvc.perform(post("/api/drugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(drug)))
            .andExpect(status().isBadRequest());

        // Validate the Drug in the database
        List<Drug> drugList = drugRepository.findAll();
        assertThat(drugList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = drugRepository.findAll().size();
        // set the field null
        drug.setName(null);

        // Create the Drug, which fails.

        restDrugMockMvc.perform(post("/api/drugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(drug)))
            .andExpect(status().isBadRequest());

        List<Drug> drugList = drugRepository.findAll();
        assertThat(drugList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFormIsRequired() throws Exception {
        int databaseSizeBeforeTest = drugRepository.findAll().size();
        // set the field null
        drug.setForm(null);

        // Create the Drug, which fails.

        restDrugMockMvc.perform(post("/api/drugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(drug)))
            .andExpect(status().isBadRequest());

        List<Drug> drugList = drugRepository.findAll();
        assertThat(drugList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDoseIsRequired() throws Exception {
        int databaseSizeBeforeTest = drugRepository.findAll().size();
        // set the field null
        drug.setDose(null);

        // Create the Drug, which fails.

        restDrugMockMvc.perform(post("/api/drugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(drug)))
            .andExpect(status().isBadRequest());

        List<Drug> drugList = drugRepository.findAll();
        assertThat(drugList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPackagingIsRequired() throws Exception {
        int databaseSizeBeforeTest = drugRepository.findAll().size();
        // set the field null
        drug.setPackaging(null);

        // Create the Drug, which fails.

        restDrugMockMvc.perform(post("/api/drugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(drug)))
            .andExpect(status().isBadRequest());

        List<Drug> drugList = drugRepository.findAll();
        assertThat(drugList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = drugRepository.findAll().size();
        // set the field null
        drug.setPrice(null);

        // Create the Drug, which fails.

        restDrugMockMvc.perform(post("/api/drugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(drug)))
            .andExpect(status().isBadRequest());

        List<Drug> drugList = drugRepository.findAll();
        assertThat(drugList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDrugs() throws Exception {
        // Initialize the database
        drugRepository.saveAndFlush(drug);

        // Get all the drugList
        restDrugMockMvc.perform(get("/api/drugs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(drug.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].form").value(hasItem(DEFAULT_FORM.toString())))
            .andExpect(jsonPath("$.[*].dose").value(hasItem(DEFAULT_DOSE.toString())))
            .andExpect(jsonPath("$.[*].packaging").value(hasItem(DEFAULT_PACKAGING.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllDrugsWithEagerRelationshipsIsEnabled() throws Exception {
        DrugResource drugResource = new DrugResource(drugServiceMock);
        when(drugServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restDrugMockMvc = MockMvcBuilders.standaloneSetup(drugResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restDrugMockMvc.perform(get("/api/drugs?eagerload=true"))
        .andExpect(status().isOk());

        verify(drugServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllDrugsWithEagerRelationshipsIsNotEnabled() throws Exception {
        DrugResource drugResource = new DrugResource(drugServiceMock);
            when(drugServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restDrugMockMvc = MockMvcBuilders.standaloneSetup(drugResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restDrugMockMvc.perform(get("/api/drugs?eagerload=true"))
        .andExpect(status().isOk());

            verify(drugServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getDrug() throws Exception {
        // Initialize the database
        drugRepository.saveAndFlush(drug);

        // Get the drug
        restDrugMockMvc.perform(get("/api/drugs/{id}", drug.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(drug.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.form").value(DEFAULT_FORM.toString()))
            .andExpect(jsonPath("$.dose").value(DEFAULT_DOSE.toString()))
            .andExpect(jsonPath("$.packaging").value(DEFAULT_PACKAGING.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDrug() throws Exception {
        // Get the drug
        restDrugMockMvc.perform(get("/api/drugs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDrug() throws Exception {
        // Initialize the database
        drugService.save(drug);

        int databaseSizeBeforeUpdate = drugRepository.findAll().size();

        // Update the drug
        Drug updatedDrug = drugRepository.findById(drug.getId()).get();
        // Disconnect from session so that the updates on updatedDrug are not directly saved in db
        em.detach(updatedDrug);
        updatedDrug
            .name(UPDATED_NAME)
            .form(UPDATED_FORM)
            .dose(UPDATED_DOSE)
            .packaging(UPDATED_PACKAGING)
            .price(UPDATED_PRICE)
            .rating(UPDATED_RATING)
            .description(UPDATED_DESCRIPTION);

        restDrugMockMvc.perform(put("/api/drugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDrug)))
            .andExpect(status().isOk());

        // Validate the Drug in the database
        List<Drug> drugList = drugRepository.findAll();
        assertThat(drugList).hasSize(databaseSizeBeforeUpdate);
        Drug testDrug = drugList.get(drugList.size() - 1);
        assertThat(testDrug.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDrug.getForm()).isEqualTo(UPDATED_FORM);
        assertThat(testDrug.getDose()).isEqualTo(UPDATED_DOSE);
        assertThat(testDrug.getPackaging()).isEqualTo(UPDATED_PACKAGING);
        assertThat(testDrug.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testDrug.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testDrug.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingDrug() throws Exception {
        int databaseSizeBeforeUpdate = drugRepository.findAll().size();

        // Create the Drug

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDrugMockMvc.perform(put("/api/drugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(drug)))
            .andExpect(status().isBadRequest());

        // Validate the Drug in the database
        List<Drug> drugList = drugRepository.findAll();
        assertThat(drugList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDrug() throws Exception {
        // Initialize the database
        drugService.save(drug);

        int databaseSizeBeforeDelete = drugRepository.findAll().size();

        // Get the drug
        restDrugMockMvc.perform(delete("/api/drugs/{id}", drug.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Drug> drugList = drugRepository.findAll();
        assertThat(drugList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Drug.class);
        Drug drug1 = new Drug();
        drug1.setId(1L);
        Drug drug2 = new Drug();
        drug2.setId(drug1.getId());
        assertThat(drug1).isEqualTo(drug2);
        drug2.setId(2L);
        assertThat(drug1).isNotEqualTo(drug2);
        drug1.setId(null);
        assertThat(drug1).isNotEqualTo(drug2);
    }
}
