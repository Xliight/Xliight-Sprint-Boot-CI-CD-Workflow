package com.ouitrips.app.repositories.security.places;

import com.ouitrips.app.entities.places.LinksTp;
import com.ouitrips.app.entities.places.LinksTpId;
import com.ouitrips.app.entities.places.Place;
import com.ouitrips.app.entities.places.PlaceTag;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LinksTpRepository extends JpaRepository<LinksTp, Integer> {
    boolean existsById(LinksTpId id);

    LinksTp findByPlaceAndPlaceTag(Place place, PlaceTag placeTag);
}

