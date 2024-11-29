package com.ouitrips.app.repositories.security.circuits;

import com.ouitrips.app.entities.circuits.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationsRepository extends JpaRepository<Location, Integer> {
    Location findByName(String name);
}
