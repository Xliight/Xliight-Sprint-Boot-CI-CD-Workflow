package com.ouitrips.app.repositories.security.geolocation;

import com.ouitrips.app.entities.places.Place;
import com.ouitrips.app.entities.places.PlaceInfoGeolocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PlaceInfoGeolocationRepository extends JpaRepository<PlaceInfoGeolocation, Long> {
   Optional<PlaceInfoGeolocation> findByPlace(Place place);

}
