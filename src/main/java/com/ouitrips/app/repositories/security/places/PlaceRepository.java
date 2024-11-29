package com.ouitrips.app.repositories.security.places;

import com.ouitrips.app.entities.places.City;
import com.ouitrips.app.entities.places.LinksPt;
import com.ouitrips.app.entities.places.Place;
import com.ouitrips.app.entities.places.PlaceType;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public interface PlaceRepository extends JpaRepository<Place, Integer> , JpaSpecificationExecutor<Place> {

    default Page<Place> search(Integer cityId, List<String> excludedPlaceType, List<String> includePlaceType, Pageable pageable) {
        Specification<Place> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (cityId != null) {
                Join<Place, City> cityJoin = root.join("city");
                predicates.add(criteriaBuilder.equal(cityJoin.get("id"), cityId));
            }
            if (excludedPlaceType != null && !excludedPlaceType.isEmpty()) {
                Join<Place, LinksPt> linksPtJoin = root.join("linksPts", JoinType.LEFT);
                Join<LinksPt, PlaceType> placeTypeJoin = linksPtJoin.join("placeType", JoinType.LEFT);

                Predicate excludedPredicate = criteriaBuilder.not(placeTypeJoin.get("code").in(excludedPlaceType));
                Predicate nullPlaceTypePredicate = criteriaBuilder.isNull(placeTypeJoin.get("code"));

                predicates.add(criteriaBuilder.or(nullPlaceTypePredicate, excludedPredicate));
            }
            if (includePlaceType != null && !includePlaceType.isEmpty()) {
                Join<Place, LinksPt> linksPtJoin = root.join("linksPts", JoinType.LEFT);
                Join<LinksPt, PlaceType> placeTypeJoin = linksPtJoin.join("placeType", JoinType.LEFT);

                Predicate includedPredicate = placeTypeJoin.get("code").in(includePlaceType);
                Predicate allowNullPlaceType = criteriaBuilder.isNull(placeTypeJoin.get("code"));

                // Allow both places with matching types and places with no place type
                predicates.add(criteriaBuilder.or(includedPredicate, allowNullPlaceType));
            }


            query.orderBy(criteriaBuilder.desc(root.get("popularity")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return findAll(spec, pageable);
    }
    Optional<Place> findByAddress(String address);


    List<Place> findByCityId(Integer id);
}
