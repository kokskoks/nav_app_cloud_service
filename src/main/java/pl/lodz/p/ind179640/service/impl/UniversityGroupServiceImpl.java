package pl.lodz.p.ind179640.service.impl;

import pl.lodz.p.ind179640.service.UniversityGroupService;
import pl.lodz.p.ind179640.domain.UniversityGroup;
import pl.lodz.p.ind179640.repository.UniversityGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing UniversityGroup.
 */
@Service
@Transactional
public class UniversityGroupServiceImpl implements UniversityGroupService{

    private final Logger log = LoggerFactory.getLogger(UniversityGroupServiceImpl.class);
    
    @Inject
    private UniversityGroupRepository universityGroupRepository;

    /**
     * Save a universityGroup.
     *
     * @param universityGroup the entity to save
     * @return the persisted entity
     */
    public UniversityGroup save(UniversityGroup universityGroup) {
        log.debug("Request to save UniversityGroup : {}", universityGroup);
        UniversityGroup result = universityGroupRepository.save(universityGroup);
        return result;
    }

    /**
     *  Get all the universityGroups.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<UniversityGroup> findAll() {
        log.debug("Request to get all UniversityGroups");
        List<UniversityGroup> result = universityGroupRepository.findAllWithEagerRelationships();

        return result;
    }

    /**
     *  Get one universityGroup by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public UniversityGroup findOne(Long id) {
        log.debug("Request to get UniversityGroup : {}", id);
        UniversityGroup universityGroup = universityGroupRepository.findOneWithEagerRelationships(id);
        return universityGroup;
    }

    /**
     *  Delete the  universityGroup by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete UniversityGroup : {}", id);
        universityGroupRepository.delete(id);
    }
}
