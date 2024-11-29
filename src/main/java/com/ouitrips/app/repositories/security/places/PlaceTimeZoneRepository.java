package com.ouitrips.app.repositories.security.places;

import com.ouitrips.app.entities.places.PlaceTimeZone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceTimeZoneRepository extends JpaRepository<PlaceTimeZone, Integer> {
    PlaceTimeZone findByName(String timeZoneName);
}
