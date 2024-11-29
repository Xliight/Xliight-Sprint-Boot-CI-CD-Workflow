package com.ouitrips.app.repositories.security.places;

import com.ouitrips.app.entities.places.City;
import com.ouitrips.app.entities.places.CitySummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CitySummaryRepository extends JpaRepository<CitySummary, Integer> {
    Optional<CitySummary> findByCity(City city);
}
