package com.ouitrips.app.dtos;

import com.ouitrips.app.dtos.circuitdto.SearchCircuitDTO;
import com.ouitrips.app.entities.circuits.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.Set;


@Mapper(componentModel = "spring")
public interface SearchCircuitMapper {

    SearchCircuitMapper INSTANCE = Mappers.getMapper(SearchCircuitMapper.class);

    @Mapping(target = "searchModelIds", expression = "java(mapSearchModelIds(searchCircuit.getSearchModels()))")
    SearchCircuitDTO toSearchCircuitDTO(SearchCircuit searchCircuit);

    List<SearchCircuitDTO> toSearchCircuitDTOs(List<SearchCircuit> searchCircuits);

    default List<Integer> mapStepIds(Set<Step> steps) {
        if (steps == null || steps.isEmpty()) {
            return Collections.emptyList();
        }
        return steps.stream()
                .map(Step::getId)
                .toList();
    }

    default List<Integer> mapSearchModelIds(List<SearchModel> searchModels) {
        if (searchModels == null || searchModels.isEmpty()) {
            return Collections.emptyList();
        }
        return searchModels.stream()
                .map(SearchModel::getId)
                .toList();
    }
}