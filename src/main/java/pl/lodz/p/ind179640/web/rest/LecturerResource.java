package pl.lodz.p.ind179640.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.lodz.p.ind179640.domain.Lecturer;
import pl.lodz.p.ind179640.service.LecturerService;
import pl.lodz.p.ind179640.service.parser.VersionUpdate;
import pl.lodz.p.ind179640.service.parser.weeia.WeeiaParser;
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
 * REST controller for managing Lecturer.
 */
@RestController
@RequestMapping("/api")
public class LecturerResource {

    private final Logger log = LoggerFactory.getLogger(LecturerResource.class);
        
    @Inject
    private LecturerService lecturerService;

    /**
     * POST  /lecturers : Create a new lecturer.
     *
     * @param lecturer the lecturer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lecturer, or with status 400 (Bad Request) if the lecturer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lecturers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @VersionUpdate(name = WeeiaParser.PARSER_NAME)
    public ResponseEntity<Lecturer> createLecturer(@RequestBody Lecturer lecturer) throws URISyntaxException {
        log.debug("REST request to save Lecturer : {}", lecturer);
        if (lecturer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lecturer", "idexists", "A new lecturer cannot already have an ID")).body(null);
        }
        Lecturer result = lecturerService.save(lecturer);
        return ResponseEntity.created(new URI("/api/lecturers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lecturer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lecturers : Updates an existing lecturer.
     *
     * @param lecturer the lecturer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lecturer,
     * or with status 400 (Bad Request) if the lecturer is not valid,
     * or with status 500 (Internal Server Error) if the lecturer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lecturers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @VersionUpdate(name = WeeiaParser.PARSER_NAME)
    public ResponseEntity<Lecturer> updateLecturer(@RequestBody Lecturer lecturer) throws URISyntaxException {
        log.debug("REST request to update Lecturer : {}", lecturer);
        if (lecturer.getId() == null) {
            return createLecturer(lecturer);
        }
        Lecturer result = lecturerService.save(lecturer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lecturer", lecturer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lecturers : get all the lecturers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of lecturers in body
     */
    @RequestMapping(value = "/lecturers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Lecturer> getAllLecturers() {
        log.debug("REST request to get all Lecturers");
        return lecturerService.findAll();
    }

    /**
     * GET  /lecturers/:id : get the "id" lecturer.
     *
     * @param id the id of the lecturer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lecturer, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lecturers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lecturer> getLecturer(@PathVariable Long id) {
        log.debug("REST request to get Lecturer : {}", id);
        Lecturer lecturer = lecturerService.findOne(id);
        return Optional.ofNullable(lecturer)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lecturers/:id : delete the "id" lecturer.
     *
     * @param id the id of the lecturer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/lecturers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @VersionUpdate(name = WeeiaParser.PARSER_NAME)
    public ResponseEntity<Void> deleteLecturer(@PathVariable Long id) {
        log.debug("REST request to delete Lecturer : {}", id);
        lecturerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lecturer", id.toString())).build();
    }

}
