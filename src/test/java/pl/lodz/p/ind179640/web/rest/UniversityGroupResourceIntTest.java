package pl.lodz.p.ind179640.web.rest;

import pl.lodz.p.ind179640.NavAppApp;

import pl.lodz.p.ind179640.domain.UniversityGroup;
import pl.lodz.p.ind179640.repository.UniversityGroupRepository;
import pl.lodz.p.ind179640.service.UniversityGroupService;

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
 * Test class for the UniversityGroupResource REST controller.
 *
 * @see UniversityGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NavAppApp.class)
public class UniversityGroupResourceIntTest {

    private static final String DEFAULT_SUBJECT = "AAAAA";
    private static final String UPDATED_SUBJECT = "BBBBB";

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Integer DEFAULT_SEMESTER = 1;
    private static final Integer UPDATED_SEMESTER = 2;

    private static final Boolean DEFAULT_SPECIALISATION = false;
    private static final Boolean UPDATED_SPECIALISATION = true;

    @Inject
    private UniversityGroupRepository universityGroupRepository;

    @Inject
    private UniversityGroupService universityGroupService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUniversityGroupMockMvc;

    private UniversityGroup universityGroup;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UniversityGroupResource universityGroupResource = new UniversityGroupResource();
        ReflectionTestUtils.setField(universityGroupResource, "universityGroupService", universityGroupService);
        this.restUniversityGroupMockMvc = MockMvcBuilders.standaloneSetup(universityGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UniversityGroup createEntity(EntityManager em) {
        UniversityGroup universityGroup = new UniversityGroup()
                .subject(DEFAULT_SUBJECT)
                .code(DEFAULT_CODE)
                .description(DEFAULT_DESCRIPTION)
                .semester(DEFAULT_SEMESTER)
                .specialisation(DEFAULT_SPECIALISATION);
        return universityGroup;
    }

    @Before
    public void initTest() {
        universityGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createUniversityGroup() throws Exception {
        int databaseSizeBeforeCreate = universityGroupRepository.findAll().size();

        // Create the UniversityGroup

        restUniversityGroupMockMvc.perform(post("/api/university-groups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(universityGroup)))
                .andExpect(status().isCreated());

        // Validate the UniversityGroup in the database
        List<UniversityGroup> universityGroups = universityGroupRepository.findAll();
        assertThat(universityGroups).hasSize(databaseSizeBeforeCreate + 1);
        UniversityGroup testUniversityGroup = universityGroups.get(universityGroups.size() - 1);
        assertThat(testUniversityGroup.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testUniversityGroup.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testUniversityGroup.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testUniversityGroup.getSemester()).isEqualTo(DEFAULT_SEMESTER);
        assertThat(testUniversityGroup.isSpecialisation()).isEqualTo(DEFAULT_SPECIALISATION);
    }

    @Test
    @Transactional
    public void getAllUniversityGroups() throws Exception {
        // Initialize the database
        universityGroupRepository.saveAndFlush(universityGroup);

        // Get all the universityGroups
        restUniversityGroupMockMvc.perform(get("/api/university-groups?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(universityGroup.getId().intValue())))
                .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].semester").value(hasItem(DEFAULT_SEMESTER)))
                .andExpect(jsonPath("$.[*].specialisation").value(hasItem(DEFAULT_SPECIALISATION.booleanValue())));
    }

    @Test
    @Transactional
    public void getUniversityGroup() throws Exception {
        // Initialize the database
        universityGroupRepository.saveAndFlush(universityGroup);

        // Get the universityGroup
        restUniversityGroupMockMvc.perform(get("/api/university-groups/{id}", universityGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(universityGroup.getId().intValue()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.semester").value(DEFAULT_SEMESTER))
            .andExpect(jsonPath("$.specialisation").value(DEFAULT_SPECIALISATION.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUniversityGroup() throws Exception {
        // Get the universityGroup
        restUniversityGroupMockMvc.perform(get("/api/university-groups/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUniversityGroup() throws Exception {
        // Initialize the database
        universityGroupService.save(universityGroup);

        int databaseSizeBeforeUpdate = universityGroupRepository.findAll().size();

        // Update the universityGroup
        UniversityGroup updatedUniversityGroup = universityGroupRepository.findOne(universityGroup.getId());
        updatedUniversityGroup
                .subject(UPDATED_SUBJECT)
                .code(UPDATED_CODE)
                .description(UPDATED_DESCRIPTION)
                .semester(UPDATED_SEMESTER)
                .specialisation(UPDATED_SPECIALISATION);

        restUniversityGroupMockMvc.perform(put("/api/university-groups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUniversityGroup)))
                .andExpect(status().isOk());

        // Validate the UniversityGroup in the database
        List<UniversityGroup> universityGroups = universityGroupRepository.findAll();
        assertThat(universityGroups).hasSize(databaseSizeBeforeUpdate);
        UniversityGroup testUniversityGroup = universityGroups.get(universityGroups.size() - 1);
        assertThat(testUniversityGroup.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testUniversityGroup.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUniversityGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUniversityGroup.getSemester()).isEqualTo(UPDATED_SEMESTER);
        assertThat(testUniversityGroup.isSpecialisation()).isEqualTo(UPDATED_SPECIALISATION);
    }

    @Test
    @Transactional
    public void deleteUniversityGroup() throws Exception {
        // Initialize the database
        universityGroupService.save(universityGroup);

        int databaseSizeBeforeDelete = universityGroupRepository.findAll().size();

        // Get the universityGroup
        restUniversityGroupMockMvc.perform(delete("/api/university-groups/{id}", universityGroup.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UniversityGroup> universityGroups = universityGroupRepository.findAll();
        assertThat(universityGroups).hasSize(databaseSizeBeforeDelete - 1);
    }
}
