package pl.lodz.p.ind179640.service;

import pl.lodz.p.ind179640.domain.Sublocation;

import java.util.List;

/**
 * Service Interface for managing Sublocation.
 */
public interface SublocationService {

    /**
     * Save a sublocation.
     *
     * @param sublocation the entity to save
     * @return the persisted entity
     */
    Sublocation save(Sublocation sublocation);

    /**
     *  Get all the sublocations.
     *  
     *  @return the list of entities
     */
    List<Sublocation> findAll();

    /**
     *  Get the "id" sublocation.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Sublocation findOne(Long id);

    /**
     *  Delete the "id" sublocation.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
