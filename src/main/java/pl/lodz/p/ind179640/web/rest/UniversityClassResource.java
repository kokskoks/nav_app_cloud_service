package pl.lodz.p.ind179640.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.lodz.p.ind179640.domain.UniversityClass;
import pl.lodz.p.ind179640.service.UniversityClassService;
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
 * REST controller for managing UniversityClass.
 */
@RestController
@RequestMapping("/api")
public class UniversityClassResource {

    private final Logger log = LoggerFactory.getLogger(UniversityClassResource.class);
        
    @Inject
    private UniversityClassService universityClassService;

    /**
     * POST  /university-classes : Create a new universityClass.
     *
     * @param universityClass the universityClass to create
     * @return the ResponseEntity with status 201 (Created) and with body the new universityClass, or with status 400 (Bad Request) if the universityClass has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/university-classes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UniversityClass> createUniversityClass(@RequestBody UniversityClass universityClass) throws URISyntaxException {
        log.debug("REST request to save UniversityClass : {}", universityClass);
        if (universityClass.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("universityClass", "idexists", "A new universityClass cannot already have an ID")).body(null);
        }
        UniversityClass result = universityClassService.save(universityClass);
        return ResponseEntity.created(new URI("/api/university-classes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("universityClass", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /university-classes : Updates an existing universityClass.
     *
     * @param universityClass the universityClass to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated universityClass,
     * or with status 400 (Bad Request) if the universityClass is not valid,
     * or with status 500 (Internal Server Error) if the universityClass couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/university-classes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UniversityClass> updateUniversityClass(@RequestBody UniversityClass universityClass) throws URISyntaxException {
        log.debug("REST request to update UniversityClass : {}", universityClass);
        if (universityClass.getId() == null) {
            return createUniversityClass(universityClass);
        }
        UniversityClass result = universityClassService.save(universityClass);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("universityClass", universityClass.getId().toString()))
            .body(result);
    }

    /**
     * GET  /university-classes : get all the universityClasses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of universityClasses in body
     */
    @RequestMapping(value = "/university-classes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UniversityClass> getAllUniversityClasses() {
        log.debug("REST request to get all UniversityClasses");
        return universityClassService.findAll();
    }

    /**
     * GET  /university-classes/:id : get the "id" universityClass.
     *
     * @param id the id of the universityClass to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the universityClass, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/university-classes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UniversityClass> getUniversityClass(@PathVariable Long id) {
        log.debug("REST request to get UniversityClass : {}", id);
        UniversityClass universityClass = universityClassService.findOne(id);
        return Optional.ofNullable(universityClass)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /university-classes/:id : delete the "id" universityClass.
     *
     * @param id the id of the universityClass to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/university-classes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUniversityClass(@PathVariable Long id) {
        log.debug("REST request to delete UniversityClass : {}", id);
        universityClassService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("universityClass", id.toString())).build();
    }

}
