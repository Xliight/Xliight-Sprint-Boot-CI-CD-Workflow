package com.ouitrips.app.repositories.security.places;

import com.ouitrips.app.entities.places.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Integer> {

    City findByName(String city);
}
