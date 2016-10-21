package pl.lodz.p.ind179640.service;

import pl.lodz.p.ind179640.domain.Week;

import java.util.List;

/**
 * Service Interface for managing Week.
 */
public interface WeekService {

    /**
     * Save a week.
     *
     * @param week the entity to save
     * @return the persisted entity
     */
    Week save(Week week);

    /**
     *  Get all the weeks.
     *  
     *  @return the list of entities
     */
    List<Week> findAll();

    /**
     *  Get the "id" week.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Week findOne(Long id);

    /**
     *  Delete the "id" week.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
