package com.ouitrips.app.entities.circuits;

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
public class LinksLsId implements Serializable {

    @Column(name = "fkid_location_start")
    private Integer fkidLocationStart;

    @Column(name = "fkid_location_end")
    private Integer fkidLocationEnd;

    @Column(name = "fkid_step")
    private Integer fkidStep;

    @Column(name = "fkid_mode")
    private Integer fkidMode;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinksLsId that = (LinksLsId) o;
        return Objects.equals(fkidLocationStart, that.fkidLocationStart) &&
                Objects.equals(fkidLocationEnd, that.fkidLocationEnd) &&
                Objects.equals(fkidStep, that.fkidStep) &&
                Objects.equals(fkidMode, that.fkidMode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fkidLocationStart, fkidLocationEnd, fkidStep, fkidMode);
    }
}
