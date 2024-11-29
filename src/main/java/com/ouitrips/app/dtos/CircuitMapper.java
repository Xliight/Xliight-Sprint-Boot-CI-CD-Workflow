package com.ouitrips.app.dtos;

import com.ouitrips.app.dtos.circuitdto.CircuitDTO;
import com.ouitrips.app.entities.agency.AgencyCircuit;
import com.ouitrips.app.entities.circuits.Circuit;
import com.ouitrips.app.entities.circuits.Step;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;


@Mapper
public interface CircuitMapper {
    CircuitMapper INSTANCE = Mappers.getMapper(CircuitMapper.class);

    @Mapping(target = "agencyCircuitIds", source = "agencyCircuits", qualifiedByName = "mapAgencyCircuitsToIds")
    @Mapping(target = "stepIds", source = "steps", qualifiedByName = "mapStepsToIds")
    @Mapping(target = "circuitGroupId", source = "circuitGroup.id")
    CircuitDTO toCircuitDTO(Circuit circuit);

    @Named("mapAgencyCircuitsToIds")
    static List<Integer> mapAgencyCircuitsToIds(List<AgencyCircuit> agencyCircuits) {
        return agencyCircuits != null
                ? agencyCircuits.stream().map(AgencyCircuit::getId).toList()
                : null;
    }

    @Named("mapStepsToIds")
    static List<Integer> mapStepsToIds(Set<Step> steps) {
        return steps != null
                ? steps.stream().map(Step::getId).toList()
                : null;
    }


}