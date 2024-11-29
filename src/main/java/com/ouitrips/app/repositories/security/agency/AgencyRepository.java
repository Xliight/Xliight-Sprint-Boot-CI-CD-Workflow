package com.ouitrips.app.repositories.security.agency;

import com.ouitrips.app.entities.agency.Agency;
import com.ouitrips.app.entities.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgencyRepository extends JpaRepository<Agency, Integer> {
    List<Agency> findAllByIsDeletedFalse(); // Get all non-deleted agencies
    boolean existsByOwnerId(Integer id);

    Agency findByOwner(User connectedUser);

    List<Agency> findByAgencyCircuitsCircuitId(Integer circuitId);
}
