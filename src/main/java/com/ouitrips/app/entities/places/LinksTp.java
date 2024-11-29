package com.ouitrips.app.entities.places;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Links_Tp",schema = "places")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LinksTp {
    @SequenceGenerator(name = "links_tp_id_generator", sequenceName = "links_tp_pkid_link_tp_seq", allocationSize = 1,schema = "places")
    @EmbeddedId
    private LinksTpId id;

    @MapsId("fkidPlace")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_place")
    private Place place;

    @MapsId("fkidPlaceTags")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_place_tags")
    private PlaceTag placeTag;


}
