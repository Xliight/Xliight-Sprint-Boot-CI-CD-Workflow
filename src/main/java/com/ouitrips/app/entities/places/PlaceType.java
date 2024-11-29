package com.ouitrips.app.entities.places;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "place_Types",schema = "places")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlaceType {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "place_type_id_generator")
    @SequenceGenerator(name = "place_type_id_generator", sequenceName = "place_pkid_type_seq", allocationSize = 1,schema = "places")
    @Id
    @Column(name = "pkid_type")
    private Integer id;
    @Column(name = "name", length = 45)
    private String name;
    @Column(name = "code", length = 45)
    private String code;


    @OneToMany(mappedBy = "placeType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<LinksPt> linksPts;
//    @ManyToMany(mappedBy = "placeTypes")
//    private Set<Place> places;
}
