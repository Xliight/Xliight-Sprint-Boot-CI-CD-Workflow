package com.ouitrips.app.entities.places;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;


@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LinksTpId implements Serializable {
    @Column(name = "fkid_place", nullable = false)
    private Integer fkidPlace;

    @Column(name = "fkid_place_tags", nullable = false)
    private Integer fkidPlaceTags;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinksTpId that = (LinksTpId) o;
        return fkidPlace.equals(that.fkidPlace) && fkidPlaceTags.equals(that.fkidPlaceTags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fkidPlace, fkidPlaceTags);
    }

}
