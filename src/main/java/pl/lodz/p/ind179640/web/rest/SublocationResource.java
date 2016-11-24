package pl.lodz.p.ind179640.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.lodz.p.ind179640.domain.Sublocation;
import pl.lodz.p.ind179640.service.SublocationService;
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
 * REST controller for managing Sublocation.
 */
@RestController
@RequestMapping("/api")
public class SublocationResource {

    private final Logger log = LoggerFactory.getLogger(SublocationResource.class);
        
    @Inject
    private SublocationService sublocationService;

    /**
     * POST  /sublocations : Create a new sublocation.
     *
     * @param sublocation the sublocation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sublocation, or with status 400 (Bad Request) if the sublocation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/sublocations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sublocation> createSublocation(@RequestBody Sublocation sublocation) throws URISyntaxException {
        log.debug("REST request to save Sublocation : {}", sublocation);
        if (sublocation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("sublocation", "idexists", "A new sublocation cannot already have an ID")).body(null);
        }
        Sublocation result = sublocationService.save(sublocation);
        return ResponseEntity.created(new URI("/api/sublocations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("sublocation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sublocations : Updates an existing sublocation.
     *
     * @param sublocation the sublocation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sublocation,
     * or with status 400 (Bad Request) if the sublocation is not valid,
     * or with status 500 (Internal Server Error) if the sublocation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/sublocations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sublocation> updateSublocation(@RequestBody Sublocation sublocation) throws URISyntaxException {
        log.debug("REST request to update Sublocation : {}", sublocation);
        if (sublocation.getId() == null) {
            return createSublocation(sublocation);
        }
        Sublocation result = sublocationService.save(sublocation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("sublocation", sublocation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sublocations : get all the sublocations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sublocations in body
     */
    @RequestMapping(value = "/sublocations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Sublocation> getAllSublocations() {
        log.debug("REST request to get all Sublocations");
        return sublocationService.findAll();
    }

    /**
     * GET  /sublocations/:id : get the "id" sublocation.
     *
     * @param id the id of the sublocation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sublocation, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/sublocations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sublocation> getSublocation(@PathVariable Long id) {
        log.debug("REST request to get Sublocation : {}", id);
        Sublocation sublocation = sublocationService.findOne(id);
        return Optional.ofNullable(sublocation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sublocations/:id : delete the "id" sublocation.
     *
     * @param id the id of the sublocation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/sublocations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSublocation(@PathVariable Long id) {
        log.debug("REST request to delete Sublocation : {}", id);
        sublocationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("sublocation", id.toString())).build();
    }

}
