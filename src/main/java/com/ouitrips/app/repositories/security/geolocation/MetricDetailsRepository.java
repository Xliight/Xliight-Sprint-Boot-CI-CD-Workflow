package com.ouitrips.app.repositories.security.geolocation;

import com.ouitrips.app.entities.geolocation.MetricDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MetricDetailsRepository extends JpaRepository<MetricDetails, Integer> {
    // Custom query methods can be added here if needed
}
