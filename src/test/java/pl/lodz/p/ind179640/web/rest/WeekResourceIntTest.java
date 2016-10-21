package pl.lodz.p.ind179640.web.rest;

import pl.lodz.p.ind179640.NavAppApp;

import pl.lodz.p.ind179640.domain.Week;
import pl.lodz.p.ind179640.repository.WeekRepository;
import pl.lodz.p.ind179640.service.WeekService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the WeekResource REST controller.
 *
 * @see WeekResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NavAppApp.class)
public class WeekResourceIntTest {

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private WeekRepository weekRepository;

    @Inject
    private WeekService weekService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restWeekMockMvc;

    private Week week;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WeekResource weekResource = new WeekResource();
        ReflectionTestUtils.setField(weekResource, "weekService", weekService);
        this.restWeekMockMvc = MockMvcBuilders.standaloneSetup(weekResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Week createEntity(EntityManager em) {
        Week week = new Week()
                .number(DEFAULT_NUMBER)
                .startDate(DEFAULT_START_DATE)
                .endDate(DEFAULT_END_DATE);
        return week;
    }

    @Before
    public void initTest() {
        week = createEntity(em);
    }

    @Test
    @Transactional
    public void createWeek() throws Exception {
        int databaseSizeBeforeCreate = weekRepository.findAll().size();

        // Create the Week

        restWeekMockMvc.perform(post("/api/weeks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(week)))
                .andExpect(status().isCreated());

        // Validate the Week in the database
        List<Week> weeks = weekRepository.findAll();
        assertThat(weeks).hasSize(databaseSizeBeforeCreate + 1);
        Week testWeek = weeks.get(weeks.size() - 1);
        assertThat(testWeek.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testWeek.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testWeek.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void getAllWeeks() throws Exception {
        // Initialize the database
        weekRepository.saveAndFlush(week);

        // Get all the weeks
        restWeekMockMvc.perform(get("/api/weeks?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(week.getId().intValue())))
                .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    public void getWeek() throws Exception {
        // Initialize the database
        weekRepository.saveAndFlush(week);

        // Get the week
        restWeekMockMvc.perform(get("/api/weeks/{id}", week.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(week.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWeek() throws Exception {
        // Get the week
        restWeekMockMvc.perform(get("/api/weeks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWeek() throws Exception {
        // Initialize the database
        weekService.save(week);

        int databaseSizeBeforeUpdate = weekRepository.findAll().size();

        // Update the week
        Week updatedWeek = weekRepository.findOne(week.getId());
        updatedWeek
                .number(UPDATED_NUMBER)
                .startDate(UPDATED_START_DATE)
                .endDate(UPDATED_END_DATE);

        restWeekMockMvc.perform(put("/api/weeks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedWeek)))
                .andExpect(status().isOk());

        // Validate the Week in the database
        List<Week> weeks = weekRepository.findAll();
        assertThat(weeks).hasSize(databaseSizeBeforeUpdate);
        Week testWeek = weeks.get(weeks.size() - 1);
        assertThat(testWeek.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testWeek.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testWeek.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void deleteWeek() throws Exception {
        // Initialize the database
        weekService.save(week);

        int databaseSizeBeforeDelete = weekRepository.findAll().size();

        // Get the week
        restWeekMockMvc.perform(delete("/api/weeks/{id}", week.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Week> weeks = weekRepository.findAll();
        assertThat(weeks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
