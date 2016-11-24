package pl.lodz.p.ind179640.repository;

import pl.lodz.p.ind179640.domain.Sublocation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Sublocation entity.
 */
@SuppressWarnings("unused")
public interface SublocationRepository extends JpaRepository<Sublocation,Long> {

}
