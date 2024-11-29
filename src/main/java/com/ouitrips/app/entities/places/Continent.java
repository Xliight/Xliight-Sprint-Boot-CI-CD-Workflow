package com.ouitrips.app.entities.places;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "continents",schema = "places")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Continent {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "continent_id_generator")
    @SequenceGenerator(name = "continent_id_generator", sequenceName = "continents_pkid_continent_seq", allocationSize = 1,schema = "places")
    @Id
    @Column(name = "pkid_continent")
    private Integer id;
    @Column(name = "name", length = 255)
    private String name;

    @OneToMany(mappedBy = "continent")
    private Set<Countries> countries;
}
