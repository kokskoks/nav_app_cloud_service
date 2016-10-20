package pl.lodz.p.ind179640.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.lodz.p.ind179640.domain.UniversityGroup;
import pl.lodz.p.ind179640.service.UniversityGroupService;
import pl.lodz.p.ind179640.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UniversityGroup.
 */
@RestController
@RequestMapping("/api")
public class UniversityGroupResource {

    private final Logger log = LoggerFactory.getLogger(UniversityGroupResource.class);
        
    @Inject
    private UniversityGroupService universityGroupService;

    /**
     * POST  /university-groups : Create a new universityGroup.
     *
     * @param universityGroup the universityGroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new universityGroup, or with status 400 (Bad Request) if the universityGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/university-groups",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UniversityGroup> createUniversityGroup(@RequestBody UniversityGroup universityGroup) throws URISyntaxException {
        log.debug("REST request to save UniversityGroup : {}", universityGroup);
        if (universityGroup.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("universityGroup", "idexists", "A new universityGroup cannot already have an ID")).body(null);
        }
        UniversityGroup result = universityGroupService.save(universityGroup);
        return ResponseEntity.created(new URI("/api/university-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("universityGroup", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /university-groups : Updates an existing universityGroup.
     *
     * @param universityGroup the universityGroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated universityGroup,
     * or with status 400 (Bad Request) if the universityGroup is not valid,
     * or with status 500 (Internal Server Error) if the universityGroup couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/university-groups",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UniversityGroup> updateUniversityGroup(@RequestBody UniversityGroup universityGroup) throws URISyntaxException {
        log.debug("REST request to update UniversityGroup : {}", universityGroup);
        if (universityGroup.getId() == null) {
            return createUniversityGroup(universityGroup);
        }
        UniversityGroup result = universityGroupService.save(universityGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("universityGroup", universityGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /university-groups : get all the universityGroups.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of universityGroups in body
     */
    @RequestMapping(value = "/university-groups",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UniversityGroup> getAllUniversityGroups() {
        log.debug("REST request to get all UniversityGroups");
        return universityGroupService.findAll();
    }

    /**
     * GET  /university-groups/:id : get the "id" universityGroup.
     *
     * @param id the id of the universityGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the universityGroup, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/university-groups/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UniversityGroup> getUniversityGroup(@PathVariable Long id) {
        log.debug("REST request to get UniversityGroup : {}", id);
        UniversityGroup universityGroup = universityGroupService.findOne(id);
        return Optional.ofNullable(universityGroup)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /university-groups/:id : delete the "id" universityGroup.
     *
     * @param id the id of the universityGroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/university-groups/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUniversityGroup(@PathVariable Long id) {
        log.debug("REST request to delete UniversityGroup : {}", id);
        universityGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("universityGroup", id.toString())).build();
    }

}
