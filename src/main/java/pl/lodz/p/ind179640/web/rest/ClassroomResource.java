package pl.lodz.p.ind179640.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.lodz.p.ind179640.domain.Classroom;
import pl.lodz.p.ind179640.service.ClassroomService;
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
 * REST controller for managing Classroom.
 */
@RestController
@RequestMapping("/api")
public class ClassroomResource {

    private final Logger log = LoggerFactory.getLogger(ClassroomResource.class);
        
    @Inject
    private ClassroomService classroomService;

    /**
     * POST  /classrooms : Create a new classroom.
     *
     * @param classroom the classroom to create
     * @return the ResponseEntity with status 201 (Created) and with body the new classroom, or with status 400 (Bad Request) if the classroom has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/classrooms",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Classroom> createClassroom(@RequestBody Classroom classroom) throws URISyntaxException {
        log.debug("REST request to save Classroom : {}", classroom);
        if (classroom.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("classroom", "idexists", "A new classroom cannot already have an ID")).body(null);
        }
        Classroom result = classroomService.save(classroom);
        return ResponseEntity.created(new URI("/api/classrooms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("classroom", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /classrooms : Updates an existing classroom.
     *
     * @param classroom the classroom to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated classroom,
     * or with status 400 (Bad Request) if the classroom is not valid,
     * or with status 500 (Internal Server Error) if the classroom couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/classrooms",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Classroom> updateClassroom(@RequestBody Classroom classroom) throws URISyntaxException {
        log.debug("REST request to update Classroom : {}", classroom);
        if (classroom.getId() == null) {
            return createClassroom(classroom);
        }
        Classroom result = classroomService.save(classroom);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("classroom", classroom.getId().toString()))
            .body(result);
    }

    /**
     * GET  /classrooms : get all the classrooms.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of classrooms in body
     */
    @RequestMapping(value = "/classrooms",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Classroom> getAllClassrooms() {
        log.debug("REST request to get all Classrooms");
        return classroomService.findAll();
    }

    /**
     * GET  /classrooms/:id : get the "id" classroom.
     *
     * @param id the id of the classroom to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the classroom, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/classrooms/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Classroom> getClassroom(@PathVariable Long id) {
        log.debug("REST request to get Classroom : {}", id);
        Classroom classroom = classroomService.findOne(id);
        return Optional.ofNullable(classroom)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /classrooms/:id : delete the "id" classroom.
     *
     * @param id the id of the classroom to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/classrooms/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteClassroom(@PathVariable Long id) {
        log.debug("REST request to delete Classroom : {}", id);
        classroomService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("classroom", id.toString())).build();
    }

}
