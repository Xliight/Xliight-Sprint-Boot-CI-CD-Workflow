package com.ouitrips.app.entities.places;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "place_time_zones",schema = "places")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlaceTimeZone {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "zone_id_generator")
    @SequenceGenerator(name = "zone_id_generator", sequenceName = "place_time_zones_pkid_zone_seq", allocationSize = 1,schema = "places")
    @Id
    @Column(name = "pkid_zone")
    private Integer id;
    @Column(name = "utc_offset")
    private String utcOffset;
    @Column(name = "name", length = 255)
    private String name;

    @OneToMany(mappedBy = "placeTimeZone")
    private Set<Place> places;
}
