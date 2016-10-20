package pl.lodz.p.ind179640.repository;

import pl.lodz.p.ind179640.domain.UniversityClass;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the UniversityClass entity.
 */
@SuppressWarnings("unused")
public interface UniversityClassRepository extends JpaRepository<UniversityClass,Long> {

    @Query("select distinct universityClass from UniversityClass universityClass left join fetch universityClass.lecturers")
    List<UniversityClass> findAllWithEagerRelationships();

    @Query("select universityClass from UniversityClass universityClass left join fetch universityClass.lecturers where universityClass.id =:id")
    UniversityClass findOneWithEagerRelationships(@Param("id") Long id);

}
