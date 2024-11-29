package com.ouitrips.app.repositories.security.circuits;

import com.ouitrips.app.entities.circuits.SearchCircuit;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SearchCircuitRepository extends JpaRepository<SearchCircuit, Integer> {
    List<SearchCircuit> findByNameContainingIgnoreCase(String name);
    Optional<SearchCircuit> findByReference(String reference);

    Optional<SearchCircuit> findByIdAndIsDeletedFalse(Integer id);

    Optional<SearchCircuit> findByReferenceAndIsDeletedFalse(String name);

    List<SearchCircuit> findByIsDeletedFalse();

    boolean existsByIdAndIsDeletedFalse(Integer id);

    @Modifying
    @Transactional
    @Query("DELETE FROM SearchCircuit sc WHERE sc.createdAt < :expiryTime")
    void deleteRecordsOlderThan(@Param("expiryTime") LocalDateTime expiryTime);
}
