package com.ouitrips.app.entities.social_network;

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
public class LinksPtgId implements Serializable {

    @Column(name = "fkid_Post", nullable = false)
    private Integer fkidPost;

    @Column(name = "fkid_Tag", nullable = false)
    private Integer fkidTag;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinksPtgId that = (LinksPtgId) o;
        return fkidPost.equals(that.fkidPost) && fkidTag.equals(that.fkidTag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fkidPost, fkidTag);
    }

}
