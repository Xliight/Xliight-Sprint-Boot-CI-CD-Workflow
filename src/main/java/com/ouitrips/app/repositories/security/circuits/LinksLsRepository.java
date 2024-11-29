package com.ouitrips.app.repositories.security.circuits;

import com.ouitrips.app.entities.circuits.LinksLs;
import com.ouitrips.app.entities.circuits.Step;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinksLsRepository extends JpaRepository<LinksLs, Integer> {
    List<LinksLs> findByStep(Step step);
}
