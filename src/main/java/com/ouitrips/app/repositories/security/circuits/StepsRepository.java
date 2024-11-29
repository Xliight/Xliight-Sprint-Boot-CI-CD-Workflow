package com.ouitrips.app.repositories.security.circuits;

import com.ouitrips.app.entities.circuits.Circuit;
import com.ouitrips.app.entities.circuits.Step;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface StepsRepository extends JpaRepository<Step, Integer>, JpaSpecificationExecutor<Step> {
   List<Step> findByCircuit(Circuit circuit, Sort sort);
}
