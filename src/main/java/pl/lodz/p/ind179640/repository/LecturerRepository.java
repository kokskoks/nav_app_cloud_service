package pl.lodz.p.ind179640.repository;

import pl.lodz.p.ind179640.domain.Lecturer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Lecturer entity.
 */
@SuppressWarnings("unused")
public interface LecturerRepository extends JpaRepository<Lecturer,Long> {

}
