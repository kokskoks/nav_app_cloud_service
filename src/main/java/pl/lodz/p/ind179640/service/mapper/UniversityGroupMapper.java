package pl.lodz.p.ind179640.service.mapper;

import pl.lodz.p.ind179640.domain.*;
import pl.lodz.p.ind179640.service.dto.UniversityGroupDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity UniversityGroup and its DTO UniversityGroupDTO.
 */
@Mapper(componentModel = "spring", uses = {UniversityClassMapper.class, })
public interface UniversityGroupMapper {

    UniversityGroupDTO universityGroupToUniversityGroupDTO(UniversityGroup universityGroup);

    List<UniversityGroupDTO> universityGroupsToUniversityGroupDTOs(List<UniversityGroup> universityGroups);

    UniversityGroup universityGroupDTOToUniversityGroup(UniversityGroupDTO universityGroupDTO);

    List<UniversityGroup> universityGroupDTOsToUniversityGroups(List<UniversityGroupDTO> universityGroupDTOs);

    default UniversityClass universityClassFromId(Long id) {
        if (id == null) {
            return null;
        }
        UniversityClass universityClass = new UniversityClass();
        universityClass.setId(id);
        return universityClass;
    }
}
