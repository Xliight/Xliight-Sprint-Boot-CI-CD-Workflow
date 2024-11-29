package com.ouitrips.app.entities.circuits;

import com.ouitrips.app.entities.places.Place;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "locations",schema = "circuits")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Location {

    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "location_id_generator")
    @SequenceGenerator(name = "location_id_generator", sequenceName = "locations_pkid_location_seq", allocationSize = 1, schema = "circuits")
    @Id
    @Column(name = "pkid_location")
    private Integer id;
    @Column(name = "description", length = 500)
    private String description;
    @Column(name = "latitude")
    private double latitude;
    @Column(name = "longitude")
    private double longitude;
    @Column(name = "address")
    private String address;
    @Column(name = "name", length = 255)
    private String name;
    @Column(name = "rating")
    private Integer rating;

    @OneToMany(mappedBy = "locationStart")
    private Set<LinksLs> linksAsStart;

    @OneToMany(mappedBy = "locationEnd")
    private Set<LinksLs> linksAsEnd;

    @ManyToOne
    @JoinColumn(name = "fkid_place")
    private Place place;
    @ManyToOne
    @JoinColumn(name = "fkid_place_spec")
    private SpecificPlace specificPlace;

}
