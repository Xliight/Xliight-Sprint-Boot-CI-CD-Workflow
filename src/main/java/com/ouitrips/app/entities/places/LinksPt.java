package com.ouitrips.app.entities.places;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "links_pt",schema = "places")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LinksPt {
    @SequenceGenerator(name = "links_pt_id_generator", sequenceName = "links_pt_pkid_link_pt_seq", allocationSize = 1,schema = "places")
    @EmbeddedId
    private LinksPtId id;

    @MapsId("places")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_place")
    private Place place;

    @MapsId("placeType")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_place_types")
    private PlaceType placeType;
}
