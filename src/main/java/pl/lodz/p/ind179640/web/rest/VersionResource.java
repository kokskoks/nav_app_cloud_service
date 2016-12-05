package pl.lodz.p.ind179640.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.lodz.p.ind179640.domain.Version;
import pl.lodz.p.ind179640.service.VersionService;
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
 * REST controller for managing Version.
 */
@RestController
@RequestMapping("/api")
public class VersionResource {

    private final Logger log = LoggerFactory.getLogger(VersionResource.class);
        
    @Inject
    private VersionService versionService;

    /**
     * POST  /versions : Create a new version.
     *
     * @param version the version to create
     * @return the ResponseEntity with status 201 (Created) and with body the new version, or with status 400 (Bad Request) if the version has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/versions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Version> createVersion(@RequestBody Version version) throws URISyntaxException {
        log.debug("REST request to save Version : {}", version);
        if (version.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("version", "idexists", "A new version cannot already have an ID")).body(null);
        }
        Version result = versionService.save(version);
        return ResponseEntity.created(new URI("/api/versions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("version", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /versions : Updates an existing version.
     *
     * @param version the version to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated version,
     * or with status 400 (Bad Request) if the version is not valid,
     * or with status 500 (Internal Server Error) if the version couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/versions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Version> updateVersion(@RequestBody Version version) throws URISyntaxException {
        log.debug("REST request to update Version : {}", version);
        if (version.getId() == null) {
            return createVersion(version);
        }
        Version result = versionService.save(version);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("version", version.getId().toString()))
            .body(result);
    }

    /**
     * GET  /versions : get all the versions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of versions in body
     */
    @RequestMapping(value = "/versions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Version> getAllVersions() {
        log.debug("REST request to get all Versions");
        return versionService.findAll();
    }

    /**
     * GET  /versions/:id : get the "id" version.
     *
     * @param id the id of the version to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the version, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/versions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Version> getVersion(@PathVariable Long id) {
        log.debug("REST request to get Version : {}", id);
        Version version = versionService.findOne(id);
        return Optional.ofNullable(version)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /versions/:id : delete the "id" version.
     *
     * @param id the id of the version to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/versions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVersion(@PathVariable Long id) {
        log.debug("REST request to delete Version : {}", id);
        versionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("version", id.toString())).build();
    }

}
