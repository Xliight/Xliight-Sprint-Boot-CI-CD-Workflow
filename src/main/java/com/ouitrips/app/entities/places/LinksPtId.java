package com.ouitrips.app.entities.places;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;


@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LinksPtId {
    @Column(name = "fkid_place", nullable = false)
    private Integer fkidPlace;

    @Column(name = "fkid_place_types", nullable = false)
    private Integer fkidPlaceTypes;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinksPtId that = (LinksPtId) o;
        return fkidPlace.equals(that.fkidPlace) && fkidPlaceTypes.equals(that.fkidPlaceTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fkidPlace, fkidPlaceTypes);
    }
}
