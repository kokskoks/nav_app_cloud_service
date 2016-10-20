package pl.lodz.p.ind179640.repository;

import pl.lodz.p.ind179640.domain.UniversityGroup;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the UniversityGroup entity.
 */
@SuppressWarnings("unused")
public interface UniversityGroupRepository extends JpaRepository<UniversityGroup,Long> {

    @Query("select distinct universityGroup from UniversityGroup universityGroup left join fetch universityGroup.classes")
    List<UniversityGroup> findAllWithEagerRelationships();

    @Query("select universityGroup from UniversityGroup universityGroup left join fetch universityGroup.classes where universityGroup.id =:id")
    UniversityGroup findOneWithEagerRelationships(@Param("id") Long id);

}
