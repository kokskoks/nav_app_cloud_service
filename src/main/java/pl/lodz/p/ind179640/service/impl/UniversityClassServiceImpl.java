package pl.lodz.p.ind179640.service.impl;

import pl.lodz.p.ind179640.service.UniversityClassService;
import pl.lodz.p.ind179640.domain.UniversityClass;
import pl.lodz.p.ind179640.repository.UniversityClassRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing UniversityClass.
 */
@Service
@Transactional
public class UniversityClassServiceImpl implements UniversityClassService{

    private final Logger log = LoggerFactory.getLogger(UniversityClassServiceImpl.class);
    
    @Inject
    private UniversityClassRepository universityClassRepository;

    /**
     * Save a universityClass.
     *
     * @param universityClass the entity to save
     * @return the persisted entity
     */
    public UniversityClass save(UniversityClass universityClass) {
        log.debug("Request to save UniversityClass : {}", universityClass);
        UniversityClass result = universityClassRepository.save(universityClass);
        return result;
    }

    /**
     *  Get all the universityClasses.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<UniversityClass> findAll() {
        log.debug("Request to get all UniversityClasses");
        List<UniversityClass> result = universityClassRepository.findAllWithEagerRelationships();

        return result;
    }

    /**
     *  Get one universityClass by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public UniversityClass findOne(Long id) {
        log.debug("Request to get UniversityClass : {}", id);
        UniversityClass universityClass = universityClassRepository.findOneWithEagerRelationships(id);
        return universityClass;
    }

    /**
     *  Delete the  universityClass by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete UniversityClass : {}", id);
        universityClassRepository.delete(id);
    }
}
