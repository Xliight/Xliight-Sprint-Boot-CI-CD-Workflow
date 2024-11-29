package com.ouitrips.app.entities.places;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "countries",schema = "places")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Countries {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "country_id_generator")
    @SequenceGenerator(name = "country_id_generator", sequenceName = "countries_pkid_country_seq", allocationSize = 1,schema = "places")
    @Id
    @Column(name = "pkid_country")
    private Integer id;
    @Column(name = "country", length = 255)
    private String country;
    @Column(name = "latitude")
    private double latitude;
    @Column(name = "longitude")
    private double longitude;
    @Column(name = "region", length = 255)
    private String region;
    @Column(name = "population")
    private int population;

    @OneToMany(mappedBy = "countries")
    private Set<City> cities;

    @ManyToOne
    @JoinColumn(name = "fkid_continent")
    private Continent continent;


}
