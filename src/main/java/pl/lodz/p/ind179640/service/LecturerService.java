package pl.lodz.p.ind179640.service;

import pl.lodz.p.ind179640.domain.Lecturer;

import java.util.List;

/**
 * Service Interface for managing Lecturer.
 */
public interface LecturerService {

    /**
     * Save a lecturer.
     *
     * @param lecturer the entity to save
     * @return the persisted entity
     */
    Lecturer save(Lecturer lecturer);

    /**
     *  Get all the lecturers.
     *  
     *  @return the list of entities
     */
    List<Lecturer> findAll();

    /**
     *  Get the "id" lecturer.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Lecturer findOne(Long id);

    /**
     *  Delete the "id" lecturer.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
