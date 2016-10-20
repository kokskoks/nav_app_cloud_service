package pl.lodz.p.ind179640.service.mapper;

import pl.lodz.p.ind179640.domain.*;
import pl.lodz.p.ind179640.service.dto.BuildingDTO;

import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper for the entity Building and its DTO BuildingDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BuildingMapper {

    BuildingDTO buildingToBuildingDTO(Building building);

    List<BuildingDTO> buildingsToBuildingDTOs(List<Building> buildings);

    @Mapping(target = "rooms", ignore = true)
    Building buildingDTOToBuilding(BuildingDTO buildingDTO);

    List<Building> buildingDTOsToBuildings(List<BuildingDTO> buildingDTOs);
}
