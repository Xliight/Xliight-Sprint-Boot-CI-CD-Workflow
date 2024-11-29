package com.ouitrips.app.repositories.security.places;

import com.ouitrips.app.entities.places.LinksPt;
import com.ouitrips.app.entities.places.LinksPtId;
import com.ouitrips.app.entities.places.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinksPtRepository extends JpaRepository<LinksPt, Integer> {
    boolean existsById(LinksPtId id);

    LinksPt findByPlace(Place place);
}
