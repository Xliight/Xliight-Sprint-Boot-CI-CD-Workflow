package com.ouitrips.app.repositories.security.circuits;


import com.ouitrips.app.entities.circuits.SpecificPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecificPlaceRepository extends JpaRepository<SpecificPlace, Integer> {
}
