package pl.lodz.p.ind179640.service.impl;

import pl.lodz.p.ind179640.service.WeekService;
import pl.lodz.p.ind179640.domain.Week;
import pl.lodz.p.ind179640.repository.WeekRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Week.
 */
@Service
@Transactional
public class WeekServiceImpl implements WeekService{

    private final Logger log = LoggerFactory.getLogger(WeekServiceImpl.class);
    
    @Inject
    private WeekRepository weekRepository;

    /**
     * Save a week.
     *
     * @param week the entity to save
     * @return the persisted entity
     */
    public Week save(Week week) {
        log.debug("Request to save Week : {}", week);
        Week result = weekRepository.save(week);
        return result;
    }

    /**
     *  Get all the weeks.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Week> findAll() {
        log.debug("Request to get all Weeks");
        List<Week> result = weekRepository.findAll();

        return result;
    }

    /**
     *  Get one week by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Week findOne(Long id) {
        log.debug("Request to get Week : {}", id);
        Week week = weekRepository.findOne(id);
        return week;
    }

    /**
     *  Delete the  week by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Week : {}", id);
        weekRepository.delete(id);
    }
}
