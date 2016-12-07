package pl.lodz.p.ind179640.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import pl.lodz.p.ind179640.domain.UniversityClass;
import pl.lodz.p.ind179640.domain.UniversityGroup;
import pl.lodz.p.ind179640.service.dto.UniversityGroupDTO;

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
