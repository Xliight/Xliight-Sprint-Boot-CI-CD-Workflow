package com.ouitrips.app.repositories.security.places;


import com.ouitrips.app.entities.places.PlaceType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PlaceTypeRepository extends JpaRepository<PlaceType, Integer> {
 PlaceType findByCode(String placeTypeCode);



}
