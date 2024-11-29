package com.ouitrips.app.dtos;
import com.ouitrips.app.dtos.circuitdto.SearchModelDTO;
import com.ouitrips.app.entities.circuits.SearchModel;
import com.ouitrips.app.entities.places.Place;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SearchModelMapper {

    SearchModelMapper INSTANCE = Mappers.getMapper(SearchModelMapper.class);

            @Mapping(target = "placesIds", expression = "java(mapPlaceIds(searchModel.getPlaces()))")
    SearchModelDTO toSearchModelDTO(SearchModel searchModel);

    List<SearchModelDTO> toSearchModelDTOs(List<SearchModel> searchModels);

    // Method to map Place objects to a list of Place IDs
    default List<Integer> mapPlaceIds(List<Place> places) {
        if (places == null || places.isEmpty()) {
            return Collections.emptyList();
        }
        return places.stream()
                .map(Place::getId) // Extract only the IDs of the places
                .toList();
    }
}
