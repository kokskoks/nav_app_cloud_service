package pl.lodz.p.ind179640.service;

import pl.lodz.p.ind179640.domain.UniversityGroup;

import java.util.List;

/**
 * Service Interface for managing UniversityGroup.
 */
public interface UniversityGroupService {

    /**
     * Save a universityGroup.
     *
     * @param universityGroup the entity to save
     * @return the persisted entity
     */
    UniversityGroup save(UniversityGroup universityGroup);

    /**
     *  Get all the universityGroups.
     *  
     *  @return the list of entities
     */
    List<UniversityGroup> findAll();

    /**
     *  Get the "id" universityGroup.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    UniversityGroup findOne(Long id);

    /**
     *  Delete the "id" universityGroup.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
