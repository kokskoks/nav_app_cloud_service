package pl.lodz.p.ind179640.service.mapper;

import pl.lodz.p.ind179640.domain.*;
import pl.lodz.p.ind179640.service.dto.ClassroomDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Classroom and its DTO ClassroomDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClassroomMapper {

    @Mapping(source = "building.id", target = "buildingId")
    ClassroomDTO classroomToClassroomDTO(Classroom classroom);

    List<ClassroomDTO> classroomsToClassroomDTOs(List<Classroom> classrooms);

    @Mapping(target = "classes", ignore = true)
    @Mapping(source = "buildingId", target = "building")
    Classroom classroomDTOToClassroom(ClassroomDTO classroomDTO);

    List<Classroom> classroomDTOsToClassrooms(List<ClassroomDTO> classroomDTOs);

    default Building buildingFromId(Long id) {
        if (id == null) {
            return null;
        }
        Building building = new Building();
        building.setId(id);
        return building;
    }
}
