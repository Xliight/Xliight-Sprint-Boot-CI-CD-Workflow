package com.ouitrips.app.entities.places;

import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PlaceInfoGeolocation",schema="geolocation")
public class PlaceInfoGeolocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "placeinfoGeo_idroute", sequenceName = "placeinfoGeo_pkid_placeinfoGeo_seq", allocationSize = 1,schema = "geolocation")
    @Column(name ="pkid_placeInfoGeo")
    private Integer id;
    @Column(name = "placeinfo")
    private String placeinfo;
    @OneToOne
    @JoinColumn(name = "fkid_placeInfoGeo", referencedColumnName = "pkid_place")
    private Place place;
}
