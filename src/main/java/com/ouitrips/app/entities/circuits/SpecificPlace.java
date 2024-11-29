package com.ouitrips.app.entities.circuits;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "specific_places",schema = "circuits")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SpecificPlace {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "specific_place_id_generator")
    @SequenceGenerator(name = "specific_place_id_generator", sequenceName = "specific_places_pkid_place_seq", allocationSize = 1, schema = "circuits")
    @Id
    @Column(name = "pkid_place")
    private Integer id;
    @Column(name = "title", length = 255)
    private String title;
    @Column(name = "description", length = 500)
    private String description;
    @Column(name = "latitude")
    private double latitude;
    @Column(name = "longitude")
    private double longitude;
    @Column(name = "address", length = 255)
    private String address;

    @OneToMany(mappedBy = "specificPlace")
    private Set<Location> locations;
}
