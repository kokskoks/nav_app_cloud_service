package pl.lodz.p.ind179640.service.impl;

import pl.lodz.p.ind179640.service.ClassroomService;
import pl.lodz.p.ind179640.domain.Classroom;
import pl.lodz.p.ind179640.repository.ClassroomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Classroom.
 */
@Service
@Transactional
public class ClassroomServiceImpl implements ClassroomService{

    private final Logger log = LoggerFactory.getLogger(ClassroomServiceImpl.class);
    
    @Inject
    private ClassroomRepository classroomRepository;

    /**
     * Save a classroom.
     *
     * @param classroom the entity to save
     * @return the persisted entity
     */
    public Classroom save(Classroom classroom) {
        log.debug("Request to save Classroom : {}", classroom);
        Classroom result = classroomRepository.save(classroom);
        return result;
    }

    /**
     *  Get all the classrooms.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Classroom> findAll() {
        log.debug("Request to get all Classrooms");
        List<Classroom> result = classroomRepository.findAll();

        return result;
    }

    /**
     *  Get one classroom by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Classroom findOne(Long id) {
        log.debug("Request to get Classroom : {}", id);
        Classroom classroom = classroomRepository.findOne(id);
        return classroom;
    }

    /**
     *  Delete the  classroom by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Classroom : {}", id);
        classroomRepository.delete(id);
    }
}
