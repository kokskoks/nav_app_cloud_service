package pl.lodz.p.ind179640.web.rest;

import pl.lodz.p.ind179640.NavAppApp;

import pl.lodz.p.ind179640.domain.Building;
import pl.lodz.p.ind179640.repository.BuildingRepository;
import pl.lodz.p.ind179640.service.BuildingService;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BuildingResource REST controller.
 *
 * @see BuildingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NavAppApp.class)
public class BuildingResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final String DEFAULT_STREET = "AAAAA";
    private static final String UPDATED_STREET = "BBBBB";

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    @Inject
    private BuildingRepository buildingRepository;

    @Inject
    private BuildingService buildingService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBuildingMockMvc;

    private Building building;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BuildingResource buildingResource = new BuildingResource();
        ReflectionTestUtils.setField(buildingResource, "buildingService", buildingService);
        this.restBuildingMockMvc = MockMvcBuilders.standaloneSetup(buildingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Building createEntity(EntityManager em) {
        Building building = new Building()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .street(DEFAULT_STREET)
                .longitude(DEFAULT_LONGITUDE)
                .latitude(DEFAULT_LATITUDE)
                .photo(DEFAULT_PHOTO)
                .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE);
        return building;
    }

    @Before
    public void initTest() {
        building = createEntity(em);
    }

    @Test
    @Transactional
    public void createBuilding() throws Exception {
        int databaseSizeBeforeCreate = buildingRepository.findAll().size();

        // Create the Building

        restBuildingMockMvc.perform(post("/api/buildings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(building)))
                .andExpect(status().isCreated());

        // Validate the Building in the database
        List<Building> buildings = buildingRepository.findAll();
        assertThat(buildings).hasSize(databaseSizeBeforeCreate + 1);
        Building testBuilding = buildings.get(buildings.size() - 1);
        assertThat(testBuilding.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBuilding.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBuilding.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testBuilding.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testBuilding.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testBuilding.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testBuilding.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllBuildings() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildings
        restBuildingMockMvc.perform(get("/api/buildings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(building.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
                .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))));
    }

    @Test
    @Transactional
    public void getBuilding() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get the building
        restBuildingMockMvc.perform(get("/api/buildings/{id}", building.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(building.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET.toString()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)));
    }

    @Test
    @Transactional
    public void getNonExistingBuilding() throws Exception {
        // Get the building
        restBuildingMockMvc.perform(get("/api/buildings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBuilding() throws Exception {
        // Initialize the database
        buildingService.save(building);

        int databaseSizeBeforeUpdate = buildingRepository.findAll().size();

        // Update the building
        Building updatedBuilding = buildingRepository.findOne(building.getId());
        updatedBuilding
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .street(UPDATED_STREET)
                .longitude(UPDATED_LONGITUDE)
                .latitude(UPDATED_LATITUDE)
                .photo(UPDATED_PHOTO)
                .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        restBuildingMockMvc.perform(put("/api/buildings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBuilding)))
                .andExpect(status().isOk());

        // Validate the Building in the database
        List<Building> buildings = buildingRepository.findAll();
        assertThat(buildings).hasSize(databaseSizeBeforeUpdate);
        Building testBuilding = buildings.get(buildings.size() - 1);
        assertThat(testBuilding.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBuilding.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBuilding.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testBuilding.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testBuilding.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testBuilding.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testBuilding.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void deleteBuilding() throws Exception {
        // Initialize the database
        buildingService.save(building);

        int databaseSizeBeforeDelete = buildingRepository.findAll().size();

        // Get the building
        restBuildingMockMvc.perform(delete("/api/buildings/{id}", building.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Building> buildings = buildingRepository.findAll();
        assertThat(buildings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
