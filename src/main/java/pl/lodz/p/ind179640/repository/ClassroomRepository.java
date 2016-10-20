package pl.lodz.p.ind179640.repository;

import pl.lodz.p.ind179640.domain.Classroom;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Classroom entity.
 */
@SuppressWarnings("unused")
public interface ClassroomRepository extends JpaRepository<Classroom,Long> {

}
