package com.ouitrips.app.repositories.security.circuits;

import com.ouitrips.app.entities.circuits.CircuitGroup;
import com.ouitrips.app.entities.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CircuitGroupRepository extends JpaRepository<CircuitGroup, Integer>, JpaSpecificationExecutor<CircuitGroup> {
    List<CircuitGroup> findByIsDefaultAndUser(Boolean isDefault, User user);
}
