package com.ouitrips.app.entities.translations;

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
public class EditorialSummaryId implements Serializable {

    @Column(name = "fkid_lang", nullable = false)
    private Integer languageId;

    @Column(name = "fkid_object", nullable = false)
    private Integer objectId;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EditorialSummaryId that = (EditorialSummaryId) o;
        return languageId.equals(that.languageId) && objectId.equals(that.objectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(languageId, objectId);
    }

}
