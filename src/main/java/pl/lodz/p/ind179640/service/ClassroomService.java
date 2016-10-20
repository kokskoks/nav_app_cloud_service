package pl.lodz.p.ind179640.service;

import pl.lodz.p.ind179640.domain.Classroom;

import java.util.List;

/**
 * Service Interface for managing Classroom.
 */
public interface ClassroomService {

    /**
     * Save a classroom.
     *
     * @param classroom the entity to save
     * @return the persisted entity
     */
    Classroom save(Classroom classroom);

    /**
     *  Get all the classrooms.
     *  
     *  @return the list of entities
     */
    List<Classroom> findAll();

    /**
     *  Get the "id" classroom.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Classroom findOne(Long id);

    /**
     *  Delete the "id" classroom.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
