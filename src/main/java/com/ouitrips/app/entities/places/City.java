package com.ouitrips.app.entities.places;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "cities",schema = "places")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class City {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "city_id_generator")
    @SequenceGenerator(name = "city_id_generator", sequenceName = "cities_pkid_city_seq", allocationSize = 1,schema = "places")
    @Id
    @Column(name = "pkid_city")
    private Integer id;
    @Column(name = "name", length = 255)
    private String name;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;
    @Column(name = "temperature")
    private Double temperature;
    @Column(name = "gps", length = 45)
    private String gps;

    @OneToMany(mappedBy = "city")
    private Set<Place> places;
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private Set<CitySummary> citySummaries;

    @ManyToOne
    @JoinColumn(name = "fkid_countries")
    private Countries countries;

}
