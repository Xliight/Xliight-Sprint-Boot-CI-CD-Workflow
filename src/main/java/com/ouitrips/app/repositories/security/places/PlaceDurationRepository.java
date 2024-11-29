package com.ouitrips.app.repositories.security.places;

import com.ouitrips.app.entities.places.Place;
import com.ouitrips.app.entities.places.PlaceDuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceDurationRepository extends JpaRepository<PlaceDuration, Integer> {
    Optional<PlaceDuration> findByPlace(Place place);
}
