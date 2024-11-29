package com.ouitrips.app.repositories.security.circuits;

import com.ouitrips.app.entities.circuits.CircuitGroup;
import com.ouitrips.app.entities.circuits.Circuit;
import com.ouitrips.app.entities.security.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import java.util.ArrayList;
import java.util.List;

public interface CircuitRepository extends JpaRepository<Circuit, Integer>, JpaSpecificationExecutor<Circuit> {
    //Find all circuit by user
    default List<Circuit> getAllByUser(User user, boolean isDeleted){
        Specification<Circuit> spec = (root, query, criteriaBuilder)->{
            List<Predicate> predicates = new ArrayList<>();
            Join<Circuit, CircuitGroup> circuitGroupJoin = root.join("circuitGroup");
            predicates.add(
                    criteriaBuilder.equal(circuitGroupJoin.get("user"), user)
            );
            if(!isDeleted)
                predicates.add(
                        criteriaBuilder.isFalse(root.get("isDeleted"))
                );
            return criteriaBuilder.and(predicates.toArray(new  Predicate[0]));
        };
        return findAll(spec);
    }
    List<Circuit> findByIsDeleted(Boolean isDeleted);

    default List<Circuit> search(String query) {
        Specification<Circuit> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.isFalse(root.get("isDeleted")));
            if (query != null && !query.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + query + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return findAll(spec);
    }
    default List<Circuit> findLiveCircuits() {
        Specification<Circuit> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.isFalse(root.get("isDeleted")));
            predicates.add(criteriaBuilder.isNotNull(root.get("dateDebut")));
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.isNull(root.get("dateFin")),
                    criteriaBuilder.greaterThan(root.get("dateFin"), criteriaBuilder.currentDate())
            ));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return findAll(spec);
    }

}
