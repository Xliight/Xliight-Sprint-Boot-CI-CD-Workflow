package com.ouitrips.app.mapper.security.places;

import com.ouitrips.app.entities.places.*;
import org.springframework.context.annotation.Configuration;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class PlaceMapper implements Function<Place, Object> {
    private final PlaceInfoMapper placeInfoMapper;
    private final PlaceDurationMapper placeDurationMapper;
    private final PlacePricingMapper placePricingMapper;
    private final PlaceOpeningHourMapper placeOpeningHourMapper;
    private final PlaceTimeZoneMapper placeTimeZoneMapper;
    private final CityMapper cityMapper;
    private final PlaceTypeMapper placeTypeMapper;
    private final PlaceTagMapper placeTagMapper;

    public PlaceMapper(PlaceInfoMapper placeInfoMapper, PlaceDurationMapper placeDurationMapper, PlacePricingMapper placePricingMapper, PlaceOpeningHourMapper placeOpeningHourMapper, PlaceTimeZoneMapper placeTimeZoneMapper, CityMapper cityMapper, PlaceTypeMapper placeTypeMapper, PlaceTagMapper placeTagMapper) {
        this.placeInfoMapper = placeInfoMapper;
        this.placeDurationMapper = placeDurationMapper;
        this.placePricingMapper = placePricingMapper;
        this.placeOpeningHourMapper = placeOpeningHourMapper;
        this.placeTimeZoneMapper = placeTimeZoneMapper;
        this.cityMapper = cityMapper;
        this.placeTypeMapper = placeTypeMapper;
        this.placeTagMapper = placeTagMapper;
    }

    @Override
    public Object apply(Place place) {
        Map<String, Object> response = new HashMap<>();
        if (place == null) {
            return response;
        }
        response.put("reference", place.getId());
        response.put("title", place.getTitle());
        response.put("description", place.getDescription());
        response.put("content", place.getContent());
        response.put("latitude", place.getLatitude());
        response.put("longitude", place.getLongitude());
        response.put("address", place.getAddress());
        response.put("popularity", place.getPopularity());
        // PlaceInfo mapping
        Set<PlaceInfo> placeInfos = place.getPlaceInfos();
        if(placeInfos != null && !placeInfos.isEmpty()){
            Set<Object> placeInfosResponse = place.getPlaceInfos().stream()
                    .map(placeInfoMapper)
                    .collect(Collectors.toSet());
            response.put("place_infos", placeInfosResponse);
        }
        // PlaceDuration mapping
        Set<PlaceDuration> placeDurations = place.getPlaceDurations();
        if (placeDurations != null && !placeDurations.isEmpty()) {
            Set<Object> placeDurationsResponse = placeDurations.stream()
                    .map(placeDurationMapper)
                    .collect(Collectors.toSet());
            response.put("place_durations", placeDurationsResponse);
        }
        // PlacePricing mapping
        Set<PlacePricing> placePricings = place.getPlacePricing();
        if (placePricings != null && !placePricings.isEmpty()) {
            Set<Object> placePricingsResponse = placePricings.stream()
                    .map(placePricingMapper)
                    .collect(Collectors.toSet());
            response.put("place_pricing", placePricingsResponse);
        }
        // PlaceOpeningHour mapping
        Set<PlaceOpeningHour> placeOpeningHours = place.getPlaceOpeningHours();
        if (placeOpeningHours != null && !placeOpeningHours.isEmpty()) {
            Set<Object> placeOpeningHoursResponse = placeOpeningHours.stream()
                    .map(placeOpeningHourMapper)
                    .collect(Collectors.toSet());
            response.put("place_opening_hours", placeOpeningHoursResponse);
        }
        // PlaceTimeZone mapping
        PlaceTimeZone placeTimeZone = place.getPlaceTimeZone();
        if (placeTimeZone != null) {
            response.put("place_time_zone", placeTimeZoneMapper.apply(placeTimeZone));
        }
        // City mapping
        City city = place.getCity();
        if (city != null) {
            response.put("city", cityMapper.apply(city));
        }

//        // PlaceTypes mapping
//        Set<PlaceType> placeTypes = place.getPlaceTypes();
//        if (placeTypes != null && !placeTypes.isEmpty()) {
//            Set<Object> placeTypesResponse = placeTypes.stream()
//                    .map(placeTypeMapper)
//                    .collect(Collectors.toSet());
//            response.put("place_types", placeTypesResponse);
//        }

        // PlaceTags mapping
        Set<PlaceTag> placeTags = place.getPlaceTags();
        if (placeTags != null && !placeTags.isEmpty()) {
            Set<Object> placeTagsResponse = placeTags.stream()
                    .map(placeTagMapper)
                    .collect(Collectors.toSet());
            response.put("place_tags", placeTagsResponse);
        }
        return response;
    }
    public Object apply2(Place place, Double duration, Double distance) {
        Map<String, Object> response = (Map<String, Object>) apply(place);
        response.put("place_traject_duration", duration);
        response.put("place_traject_distance", distance);
        return response;
    }

}
