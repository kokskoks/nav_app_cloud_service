package pl.lodz.p.ind179640.service.mapper;

import pl.lodz.p.ind179640.domain.*;
import pl.lodz.p.ind179640.service.dto.LecturerDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Lecturer and its DTO LecturerDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LecturerMapper {

    LecturerDTO lecturerToLecturerDTO(Lecturer lecturer);

    List<LecturerDTO> lecturersToLecturerDTOs(List<Lecturer> lecturers);

    @Mapping(target = "classes", ignore = true)
    Lecturer lecturerDTOToLecturer(LecturerDTO lecturerDTO);

    List<Lecturer> lecturerDTOsToLecturers(List<LecturerDTO> lecturerDTOs);
}
