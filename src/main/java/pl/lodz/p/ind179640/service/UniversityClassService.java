package pl.lodz.p.ind179640.service;

import pl.lodz.p.ind179640.domain.UniversityClass;

import java.util.List;

/**
 * Service Interface for managing UniversityClass.
 */
public interface UniversityClassService {

    /**
     * Save a universityClass.
     *
     * @param universityClass the entity to save
     * @return the persisted entity
     */
    UniversityClass save(UniversityClass universityClass);

    /**
     *  Get all the universityClasses.
     *  
     *  @return the list of entities
     */
    List<UniversityClass> findAll();

    /**
     *  Get the "id" universityClass.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    UniversityClass findOne(Long id);

    /**
     *  Delete the "id" universityClass.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
