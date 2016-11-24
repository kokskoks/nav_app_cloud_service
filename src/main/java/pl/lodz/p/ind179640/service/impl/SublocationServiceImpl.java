package pl.lodz.p.ind179640.service.impl;

import pl.lodz.p.ind179640.service.SublocationService;
import pl.lodz.p.ind179640.domain.Sublocation;
import pl.lodz.p.ind179640.repository.SublocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Sublocation.
 */
@Service
@Transactional
public class SublocationServiceImpl implements SublocationService{

    private final Logger log = LoggerFactory.getLogger(SublocationServiceImpl.class);
    
    @Inject
    private SublocationRepository sublocationRepository;

    /**
     * Save a sublocation.
     *
     * @param sublocation the entity to save
     * @return the persisted entity
     */
    public Sublocation save(Sublocation sublocation) {
        log.debug("Request to save Sublocation : {}", sublocation);
        Sublocation result = sublocationRepository.save(sublocation);
        return result;
    }

    /**
     *  Get all the sublocations.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Sublocation> findAll() {
        log.debug("Request to get all Sublocations");
        List<Sublocation> result = sublocationRepository.findAll();

        return result;
    }

    /**
     *  Get one sublocation by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Sublocation findOne(Long id) {
        log.debug("Request to get Sublocation : {}", id);
        Sublocation sublocation = sublocationRepository.findOne(id);
        return sublocation;
    }

    /**
     *  Delete the  sublocation by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Sublocation : {}", id);
        sublocationRepository.delete(id);
    }
}
