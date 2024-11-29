package com.ouitrips.app.repositories.security.geolocation;

import com.ouitrips.app.entities.geolocation.RequestAPI;
import com.ouitrips.app.entities.geolocation.ResponseAPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResponseAPIRepository extends JpaRepository<ResponseAPI, Integer> {
    Optional<ResponseAPI> findByRequestAPI(RequestAPI requestAPI);
    List<ResponseAPI> findByRequestAPIIn(List<RequestAPI> requestAPIs);

}
