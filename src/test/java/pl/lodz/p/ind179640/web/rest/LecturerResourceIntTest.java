package pl.lodz.p.ind179640.web.rest;

import pl.lodz.p.ind179640.NavAppApp;

import pl.lodz.p.ind179640.domain.Lecturer;
import pl.lodz.p.ind179640.repository.LecturerRepository;
import pl.lodz.p.ind179640.service.LecturerService;

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
 * Test class for the LecturerResource REST controller.
 *
 * @see LecturerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NavAppApp.class)
public class LecturerResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final String DEFAULT_MAIL = "AAAAA";
    private static final String UPDATED_MAIL = "BBBBB";

    @Inject
    private LecturerRepository lecturerRepository;

    @Inject
    private LecturerService lecturerService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLecturerMockMvc;

    private Lecturer lecturer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LecturerResource lecturerResource = new LecturerResource();
        ReflectionTestUtils.setField(lecturerResource, "lecturerService", lecturerService);
        this.restLecturerMockMvc = MockMvcBuilders.standaloneSetup(lecturerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lecturer createEntity(EntityManager em) {
        Lecturer lecturer = new Lecturer()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .title(DEFAULT_TITLE)
                .description(DEFAULT_DESCRIPTION)
                .mail(DEFAULT_MAIL);
        return lecturer;
    }

    @Before
    public void initTest() {
        lecturer = createEntity(em);
    }

    @Test
    @Transactional
    public void createLecturer() throws Exception {
        int databaseSizeBeforeCreate = lecturerRepository.findAll().size();

        // Create the Lecturer

        restLecturerMockMvc.perform(post("/api/lecturers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lecturer)))
                .andExpect(status().isCreated());

        // Validate the Lecturer in the database
        List<Lecturer> lecturers = lecturerRepository.findAll();
        assertThat(lecturers).hasSize(databaseSizeBeforeCreate + 1);
        Lecturer testLecturer = lecturers.get(lecturers.size() - 1);
        assertThat(testLecturer.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testLecturer.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testLecturer.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testLecturer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLecturer.getMail()).isEqualTo(DEFAULT_MAIL);
    }

    @Test
    @Transactional
    public void getAllLecturers() throws Exception {
        // Initialize the database
        lecturerRepository.saveAndFlush(lecturer);

        // Get all the lecturers
        restLecturerMockMvc.perform(get("/api/lecturers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lecturer.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL.toString())));
    }

    @Test
    @Transactional
    public void getLecturer() throws Exception {
        // Initialize the database
        lecturerRepository.saveAndFlush(lecturer);

        // Get the lecturer
        restLecturerMockMvc.perform(get("/api/lecturers/{id}", lecturer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lecturer.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.mail").value(DEFAULT_MAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLecturer() throws Exception {
        // Get the lecturer
        restLecturerMockMvc.perform(get("/api/lecturers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLecturer() throws Exception {
        // Initialize the database
        lecturerService.save(lecturer);

        int databaseSizeBeforeUpdate = lecturerRepository.findAll().size();

        // Update the lecturer
        Lecturer updatedLecturer = lecturerRepository.findOne(lecturer.getId());
        updatedLecturer
                .firstName(UPDATED_FIRST_NAME)
                .lastName(UPDATED_LAST_NAME)
                .title(UPDATED_TITLE)
                .description(UPDATED_DESCRIPTION)
                .mail(UPDATED_MAIL);

        restLecturerMockMvc.perform(put("/api/lecturers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLecturer)))
                .andExpect(status().isOk());

        // Validate the Lecturer in the database
        List<Lecturer> lecturers = lecturerRepository.findAll();
        assertThat(lecturers).hasSize(databaseSizeBeforeUpdate);
        Lecturer testLecturer = lecturers.get(lecturers.size() - 1);
        assertThat(testLecturer.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testLecturer.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testLecturer.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testLecturer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLecturer.getMail()).isEqualTo(UPDATED_MAIL);
    }

    @Test
    @Transactional
    public void deleteLecturer() throws Exception {
        // Initialize the database
        lecturerService.save(lecturer);

        int databaseSizeBeforeDelete = lecturerRepository.findAll().size();

        // Get the lecturer
        restLecturerMockMvc.perform(delete("/api/lecturers/{id}", lecturer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Lecturer> lecturers = lecturerRepository.findAll();
        assertThat(lecturers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
