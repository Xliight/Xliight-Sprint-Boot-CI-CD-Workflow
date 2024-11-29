package com.ouitrips.app.repositories.security.geolocation;

import com.ouitrips.app.entities.geolocation.RequestAPI;
import com.ouitrips.app.entities.geolocation.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Integer> {
    List<Route> findByRequestAPI(RequestAPI requestAPI);
    // Custom query methods can be added here if needed

}
