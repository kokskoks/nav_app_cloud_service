package pl.lodz.p.ind179640.web.rest;

import pl.lodz.p.ind179640.NavAppApp;

import pl.lodz.p.ind179640.domain.Sublocation;
import pl.lodz.p.ind179640.repository.SublocationRepository;
import pl.lodz.p.ind179640.service.SublocationService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SublocationResource REST controller.
 *
 * @see SublocationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NavAppApp.class)
public class SublocationResourceIntTest {

    private static final Integer DEFAULT_CODE = 1;
    private static final Integer UPDATED_CODE = 2;

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private SublocationRepository sublocationRepository;

    @Inject
    private SublocationService sublocationService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSublocationMockMvc;

    private Sublocation sublocation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SublocationResource sublocationResource = new SublocationResource();
        ReflectionTestUtils.setField(sublocationResource, "sublocationService", sublocationService);
        this.restSublocationMockMvc = MockMvcBuilders.standaloneSetup(sublocationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sublocation createEntity(EntityManager em) {
        Sublocation sublocation = new Sublocation()
                .code(DEFAULT_CODE)
                .name(DEFAULT_NAME);
        return sublocation;
    }

    @Before
    public void initTest() {
        sublocation = createEntity(em);
    }

    @Test
    @Transactional
    public void createSublocation() throws Exception {
        int databaseSizeBeforeCreate = sublocationRepository.findAll().size();

        // Create the Sublocation

        restSublocationMockMvc.perform(post("/api/sublocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sublocation)))
                .andExpect(status().isCreated());

        // Validate the Sublocation in the database
        List<Sublocation> sublocations = sublocationRepository.findAll();
        assertThat(sublocations).hasSize(databaseSizeBeforeCreate + 1);
        Sublocation testSublocation = sublocations.get(sublocations.size() - 1);
        assertThat(testSublocation.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSublocation.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllSublocations() throws Exception {
        // Initialize the database
        sublocationRepository.saveAndFlush(sublocation);

        // Get all the sublocations
        restSublocationMockMvc.perform(get("/api/sublocations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(sublocation.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getSublocation() throws Exception {
        // Initialize the database
        sublocationRepository.saveAndFlush(sublocation);

        // Get the sublocation
        restSublocationMockMvc.perform(get("/api/sublocations/{id}", sublocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sublocation.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSublocation() throws Exception {
        // Get the sublocation
        restSublocationMockMvc.perform(get("/api/sublocations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSublocation() throws Exception {
        // Initialize the database
        sublocationService.save(sublocation);

        int databaseSizeBeforeUpdate = sublocationRepository.findAll().size();

        // Update the sublocation
        Sublocation updatedSublocation = sublocationRepository.findOne(sublocation.getId());
        updatedSublocation
                .code(UPDATED_CODE)
                .name(UPDATED_NAME);

        restSublocationMockMvc.perform(put("/api/sublocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSublocation)))
                .andExpect(status().isOk());

        // Validate the Sublocation in the database
        List<Sublocation> sublocations = sublocationRepository.findAll();
        assertThat(sublocations).hasSize(databaseSizeBeforeUpdate);
        Sublocation testSublocation = sublocations.get(sublocations.size() - 1);
        assertThat(testSublocation.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSublocation.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteSublocation() throws Exception {
        // Initialize the database
        sublocationService.save(sublocation);

        int databaseSizeBeforeDelete = sublocationRepository.findAll().size();

        // Get the sublocation
        restSublocationMockMvc.perform(delete("/api/sublocations/{id}", sublocation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Sublocation> sublocations = sublocationRepository.findAll();
        assertThat(sublocations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
