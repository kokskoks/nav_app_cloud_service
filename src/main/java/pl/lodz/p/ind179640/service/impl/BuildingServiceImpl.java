package pl.lodz.p.ind179640.service.impl;

import pl.lodz.p.ind179640.service.BuildingService;
import pl.lodz.p.ind179640.domain.Building;
import pl.lodz.p.ind179640.repository.BuildingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Building.
 */
@Service
@Transactional
public class BuildingServiceImpl implements BuildingService{

    private final Logger log = LoggerFactory.getLogger(BuildingServiceImpl.class);
    
    @Inject
    private BuildingRepository buildingRepository;

    /**
     * Save a building.
     *
     * @param building the entity to save
     * @return the persisted entity
     */
    public Building save(Building building) {
        log.debug("Request to save Building : {}", building);
        Building result = buildingRepository.save(building);
        return result;
    }

    /**
     *  Get all the buildings.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Building> findAll() {
        log.debug("Request to get all Buildings");
        List<Building> result = buildingRepository.findAll();

        return result;
    }

    /**
     *  Get one building by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Building findOne(Long id) {
        log.debug("Request to get Building : {}", id);
        Building building = buildingRepository.findOne(id);
        return building;
    }

    /**
     *  Delete the  building by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Building : {}", id);
        buildingRepository.delete(id);
    }
}
