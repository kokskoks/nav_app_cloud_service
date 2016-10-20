package pl.lodz.p.ind179640.service.mapper;

import pl.lodz.p.ind179640.domain.*;
import pl.lodz.p.ind179640.service.dto.UniversityClassDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity UniversityClass and its DTO UniversityClassDTO.
 */
@Mapper(componentModel = "spring", uses = {LecturerMapper.class, })
public interface UniversityClassMapper {

    @Mapping(source = "classroom.id", target = "classroomId")
    UniversityClassDTO universityClassToUniversityClassDTO(UniversityClass universityClass);

    List<UniversityClassDTO> universityClassesToUniversityClassDTOs(List<UniversityClass> universityClasses);

    @Mapping(source = "classroomId", target = "classroom")
    @Mapping(target = "groups", ignore = true)
    UniversityClass universityClassDTOToUniversityClass(UniversityClassDTO universityClassDTO);

    List<UniversityClass> universityClassDTOsToUniversityClasses(List<UniversityClassDTO> universityClassDTOs);

    default Lecturer lecturerFromId(Long id) {
        if (id == null) {
            return null;
        }
        Lecturer lecturer = new Lecturer();
        lecturer.setId(id);
        return lecturer;
    }

    default Classroom classroomFromId(Long id) {
        if (id == null) {
            return null;
        }
        Classroom classroom = new Classroom();
        classroom.setId(id);
        return classroom;
    }
}
