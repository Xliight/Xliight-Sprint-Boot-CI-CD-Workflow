package com.ouitrips.app.repositories.security.places;

import com.ouitrips.app.entities.places.PlacePricing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlacePricingRepository extends JpaRepository<PlacePricing, Integer> {
}
