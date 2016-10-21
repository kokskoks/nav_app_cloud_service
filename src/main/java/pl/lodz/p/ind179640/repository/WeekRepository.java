package pl.lodz.p.ind179640.repository;

import pl.lodz.p.ind179640.domain.Week;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Week entity.
 */
@SuppressWarnings("unused")
public interface WeekRepository extends JpaRepository<Week,Long> {

}
