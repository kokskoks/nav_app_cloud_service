package pl.lodz.p.ind179640.web.rest;

import pl.lodz.p.ind179640.NavAppApp;

import pl.lodz.p.ind179640.domain.UniversityClass;
import pl.lodz.p.ind179640.repository.UniversityClassRepository;
import pl.lodz.p.ind179640.service.UniversityClassService;

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

import pl.lodz.p.ind179640.domain.enumeration.ClassType;
import pl.lodz.p.ind179640.domain.enumeration.Weekday;
/**
 * Test class for the UniversityClassResource REST controller.
 *
 * @see UniversityClassResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NavAppApp.class)
public class UniversityClassResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_MODULE_CODE = "AAAAA";
    private static final String UPDATED_MODULE_CODE = "BBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final ClassType DEFAULT_TYPE = ClassType.LECTURE;
    private static final ClassType UPDATED_TYPE = ClassType.LABORATORY;

    private static final Integer DEFAULT_START_HOUR = 1;
    private static final Integer UPDATED_START_HOUR = 2;

    private static final Integer DEFAULT_END_HOUR = 1;
    private static final Integer UPDATED_END_HOUR = 2;

    private static final Weekday DEFAULT_WEEKDAY = Weekday.MONDAY;
    private static final Weekday UPDATED_WEEKDAY = Weekday.TUESDAY;

    @Inject
    private UniversityClassRepository universityClassRepository;

    @Inject
    private UniversityClassService universityClassService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUniversityClassMockMvc;

    private UniversityClass universityClass;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UniversityClassResource universityClassResource = new UniversityClassResource();
        ReflectionTestUtils.setField(universityClassResource, "universityClassService", universityClassService);
        this.restUniversityClassMockMvc = MockMvcBuilders.standaloneSetup(universityClassResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UniversityClass createEntity(EntityManager em) {
        UniversityClass universityClass = new UniversityClass()
                .name(DEFAULT_NAME)
                .moduleCode(DEFAULT_MODULE_CODE)
                .description(DEFAULT_DESCRIPTION)
                .type(DEFAULT_TYPE)
                .startHour(DEFAULT_START_HOUR)
                .endHour(DEFAULT_END_HOUR)
                .weekday(DEFAULT_WEEKDAY);
        return universityClass;
    }

    @Before
    public void initTest() {
        universityClass = createEntity(em);
    }

    @Test
    @Transactional
    public void createUniversityClass() throws Exception {
        int databaseSizeBeforeCreate = universityClassRepository.findAll().size();

        // Create the UniversityClass

        restUniversityClassMockMvc.perform(post("/api/university-classes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(universityClass)))
                .andExpect(status().isCreated());

        // Validate the UniversityClass in the database
        List<UniversityClass> universityClasses = universityClassRepository.findAll();
        assertThat(universityClasses).hasSize(databaseSizeBeforeCreate + 1);
        UniversityClass testUniversityClass = universityClasses.get(universityClasses.size() - 1);
        assertThat(testUniversityClass.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUniversityClass.getModuleCode()).isEqualTo(DEFAULT_MODULE_CODE);
        assertThat(testUniversityClass.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testUniversityClass.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testUniversityClass.getStartHour()).isEqualTo(DEFAULT_START_HOUR);
        assertThat(testUniversityClass.getEndHour()).isEqualTo(DEFAULT_END_HOUR);
        assertThat(testUniversityClass.getWeekday()).isEqualTo(DEFAULT_WEEKDAY);
    }

    @Test
    @Transactional
    public void getAllUniversityClasses() throws Exception {
        // Initialize the database
        universityClassRepository.saveAndFlush(universityClass);

        // Get all the universityClasses
        restUniversityClassMockMvc.perform(get("/api/university-classes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(universityClass.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].moduleCode").value(hasItem(DEFAULT_MODULE_CODE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].startHour").value(hasItem(DEFAULT_START_HOUR)))
                .andExpect(jsonPath("$.[*].endHour").value(hasItem(DEFAULT_END_HOUR)))
                .andExpect(jsonPath("$.[*].weekday").value(hasItem(DEFAULT_WEEKDAY.toString())));
    }

    @Test
    @Transactional
    public void getUniversityClass() throws Exception {
        // Initialize the database
        universityClassRepository.saveAndFlush(universityClass);

        // Get the universityClass
        restUniversityClassMockMvc.perform(get("/api/university-classes/{id}", universityClass.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(universityClass.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.moduleCode").value(DEFAULT_MODULE_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.startHour").value(DEFAULT_START_HOUR))
            .andExpect(jsonPath("$.endHour").value(DEFAULT_END_HOUR))
            .andExpect(jsonPath("$.weekday").value(DEFAULT_WEEKDAY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUniversityClass() throws Exception {
        // Get the universityClass
        restUniversityClassMockMvc.perform(get("/api/university-classes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUniversityClass() throws Exception {
        // Initialize the database
        universityClassService.save(universityClass);

        int databaseSizeBeforeUpdate = universityClassRepository.findAll().size();

        // Update the universityClass
        UniversityClass updatedUniversityClass = universityClassRepository.findOne(universityClass.getId());
        updatedUniversityClass
                .name(UPDATED_NAME)
                .moduleCode(UPDATED_MODULE_CODE)
                .description(UPDATED_DESCRIPTION)
                .type(UPDATED_TYPE)
                .startHour(UPDATED_START_HOUR)
                .endHour(UPDATED_END_HOUR)
                .weekday(UPDATED_WEEKDAY);

        restUniversityClassMockMvc.perform(put("/api/university-classes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUniversityClass)))
                .andExpect(status().isOk());

        // Validate the UniversityClass in the database
        List<UniversityClass> universityClasses = universityClassRepository.findAll();
        assertThat(universityClasses).hasSize(databaseSizeBeforeUpdate);
        UniversityClass testUniversityClass = universityClasses.get(universityClasses.size() - 1);
        assertThat(testUniversityClass.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUniversityClass.getModuleCode()).isEqualTo(UPDATED_MODULE_CODE);
        assertThat(testUniversityClass.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUniversityClass.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testUniversityClass.getStartHour()).isEqualTo(UPDATED_START_HOUR);
        assertThat(testUniversityClass.getEndHour()).isEqualTo(UPDATED_END_HOUR);
        assertThat(testUniversityClass.getWeekday()).isEqualTo(UPDATED_WEEKDAY);
    }

    @Test
    @Transactional
    public void deleteUniversityClass() throws Exception {
        // Initialize the database
        universityClassService.save(universityClass);

        int databaseSizeBeforeDelete = universityClassRepository.findAll().size();

        // Get the universityClass
        restUniversityClassMockMvc.perform(delete("/api/university-classes/{id}", universityClass.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UniversityClass> universityClasses = universityClassRepository.findAll();
        assertThat(universityClasses).hasSize(databaseSizeBeforeDelete - 1);
    }
}
