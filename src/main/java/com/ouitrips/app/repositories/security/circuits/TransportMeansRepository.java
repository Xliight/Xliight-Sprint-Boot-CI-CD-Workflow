package com.ouitrips.app.repositories.security.circuits;

import com.ouitrips.app.entities.circuits.TransportMeans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportMeansRepository extends JpaRepository<TransportMeans, Integer> {
}
