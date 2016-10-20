package pl.lodz.p.ind179640.web.rest;

import pl.lodz.p.ind179640.NavAppApp;

import pl.lodz.p.ind179640.domain.Classroom;
import pl.lodz.p.ind179640.repository.ClassroomRepository;
import pl.lodz.p.ind179640.service.ClassroomService;

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
 * Test class for the ClassroomResource REST controller.
 *
 * @see ClassroomResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NavAppApp.class)
public class ClassroomResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_DESRIPTION = "AAAAA";
    private static final String UPDATED_DESRIPTION = "BBBBB";

    private static final Integer DEFAULT_FLOOR = 1;
    private static final Integer UPDATED_FLOOR = 2;

    @Inject
    private ClassroomRepository classroomRepository;

    @Inject
    private ClassroomService classroomService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restClassroomMockMvc;

    private Classroom classroom;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClassroomResource classroomResource = new ClassroomResource();
        ReflectionTestUtils.setField(classroomResource, "classroomService", classroomService);
        this.restClassroomMockMvc = MockMvcBuilders.standaloneSetup(classroomResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classroom createEntity(EntityManager em) {
        Classroom classroom = new Classroom()
                .name(DEFAULT_NAME)
                .desription(DEFAULT_DESRIPTION)
                .floor(DEFAULT_FLOOR);
        return classroom;
    }

    @Before
    public void initTest() {
        classroom = createEntity(em);
    }

    @Test
    @Transactional
    public void createClassroom() throws Exception {
        int databaseSizeBeforeCreate = classroomRepository.findAll().size();

        // Create the Classroom

        restClassroomMockMvc.perform(post("/api/classrooms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(classroom)))
                .andExpect(status().isCreated());

        // Validate the Classroom in the database
        List<Classroom> classrooms = classroomRepository.findAll();
        assertThat(classrooms).hasSize(databaseSizeBeforeCreate + 1);
        Classroom testClassroom = classrooms.get(classrooms.size() - 1);
        assertThat(testClassroom.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClassroom.getDesription()).isEqualTo(DEFAULT_DESRIPTION);
        assertThat(testClassroom.getFloor()).isEqualTo(DEFAULT_FLOOR);
    }

    @Test
    @Transactional
    public void getAllClassrooms() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get all the classrooms
        restClassroomMockMvc.perform(get("/api/classrooms?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(classroom.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].desription").value(hasItem(DEFAULT_DESRIPTION.toString())))
                .andExpect(jsonPath("$.[*].floor").value(hasItem(DEFAULT_FLOOR)));
    }

    @Test
    @Transactional
    public void getClassroom() throws Exception {
        // Initialize the database
        classroomRepository.saveAndFlush(classroom);

        // Get the classroom
        restClassroomMockMvc.perform(get("/api/classrooms/{id}", classroom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(classroom.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.desription").value(DEFAULT_DESRIPTION.toString()))
            .andExpect(jsonPath("$.floor").value(DEFAULT_FLOOR));
    }

    @Test
    @Transactional
    public void getNonExistingClassroom() throws Exception {
        // Get the classroom
        restClassroomMockMvc.perform(get("/api/classrooms/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassroom() throws Exception {
        // Initialize the database
        classroomService.save(classroom);

        int databaseSizeBeforeUpdate = classroomRepository.findAll().size();

        // Update the classroom
        Classroom updatedClassroom = classroomRepository.findOne(classroom.getId());
        updatedClassroom
                .name(UPDATED_NAME)
                .desription(UPDATED_DESRIPTION)
                .floor(UPDATED_FLOOR);

        restClassroomMockMvc.perform(put("/api/classrooms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedClassroom)))
                .andExpect(status().isOk());

        // Validate the Classroom in the database
        List<Classroom> classrooms = classroomRepository.findAll();
        assertThat(classrooms).hasSize(databaseSizeBeforeUpdate);
        Classroom testClassroom = classrooms.get(classrooms.size() - 1);
        assertThat(testClassroom.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClassroom.getDesription()).isEqualTo(UPDATED_DESRIPTION);
        assertThat(testClassroom.getFloor()).isEqualTo(UPDATED_FLOOR);
    }

    @Test
    @Transactional
    public void deleteClassroom() throws Exception {
        // Initialize the database
        classroomService.save(classroom);

        int databaseSizeBeforeDelete = classroomRepository.findAll().size();

        // Get the classroom
        restClassroomMockMvc.perform(delete("/api/classrooms/{id}", classroom.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Classroom> classrooms = classroomRepository.findAll();
        assertThat(classrooms).hasSize(databaseSizeBeforeDelete - 1);
    }
}
