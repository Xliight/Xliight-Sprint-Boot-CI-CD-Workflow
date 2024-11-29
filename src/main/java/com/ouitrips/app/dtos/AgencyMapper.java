package com.ouitrips.app.dtos;

import com.ouitrips.app.dtos.agencydto.AgencyDTO;
import com.ouitrips.app.entities.agency.Agency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AgencyMapper {
    AgencyMapper INSTANCE = Mappers.getMapper(AgencyMapper.class);

    @Mapping(target = "ownerId", source = "owner.id")  // Assuming the owner is a User entity with an ID
    AgencyDTO agencyToAgencyDTO(Agency agency);

    @Mapping(target = "owner.id", source = "ownerId")
    Agency agencyDTOToAgency(AgencyDTO agencyDTO);
}
