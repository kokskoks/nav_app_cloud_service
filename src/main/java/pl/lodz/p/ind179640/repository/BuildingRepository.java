package pl.lodz.p.ind179640.repository;

import pl.lodz.p.ind179640.domain.Building;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Building entity.
 */
@SuppressWarnings("unused")
public interface BuildingRepository extends JpaRepository<Building,Long> {

}
