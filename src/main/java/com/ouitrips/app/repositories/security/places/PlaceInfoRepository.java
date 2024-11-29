package com.ouitrips.app.repositories.security.places;

import com.ouitrips.app.entities.places.PlaceInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceInfoRepository extends JpaRepository<PlaceInfo, Integer> {
}
