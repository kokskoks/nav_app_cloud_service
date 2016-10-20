package pl.lodz.p.ind179640.service;

import pl.lodz.p.ind179640.domain.Building;

import java.util.List;

/**
 * Service Interface for managing Building.
 */
public interface BuildingService {

    /**
     * Save a building.
     *
     * @param building the entity to save
     * @return the persisted entity
     */
    Building save(Building building);

    /**
     *  Get all the buildings.
     *  
     *  @return the list of entities
     */
    List<Building> findAll();

    /**
     *  Get the "id" building.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Building findOne(Long id);

    /**
     *  Delete the "id" building.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
