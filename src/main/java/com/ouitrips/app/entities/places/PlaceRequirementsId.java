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
public class PlaceRequirementsId implements Serializable {
    @Column(name = "fkid_requirement_questions", nullable = false)
    private Integer fkidRequirementQuestions;
    @Column(name = "fkid_place", nullable = false)
    private Integer fkidPlace;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceRequirementsId that = (PlaceRequirementsId) o;
        return fkidRequirementQuestions.equals(that.fkidRequirementQuestions) &&
                fkidPlace.equals(that.fkidPlace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fkidRequirementQuestions, fkidPlace);
    }
}
