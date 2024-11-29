package com.ouitrips.app.repositories.security.places;

import com.ouitrips.app.entities.places.PlaceTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceTagRepository extends JpaRepository<PlaceTag, Integer> {
    PlaceTag findByTag(String placeTag);
}
