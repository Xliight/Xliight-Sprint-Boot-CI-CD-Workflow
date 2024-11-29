package com.ouitrips.app.repositories.security.circuits;


import com.ouitrips.app.entities.circuits.SearchModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchModelRepository extends JpaRepository<SearchModel, Integer> {
   SearchModel findByReference(String  ref);
}