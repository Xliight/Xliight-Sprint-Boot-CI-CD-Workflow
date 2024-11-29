package com.ouitrips.app.entities.places;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "place_free_or_not",schema = "places")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlaceFreeOrNot {//TODO: entity a rectifier
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "place_free_or_not_id_generator")
    @SequenceGenerator(name = "place_free_or_not_id_generator", sequenceName = "place_free_or_not_seq", allocationSize = 1,schema = "places")
    @Column(name = "pkid_place_free_or_not")
    private Integer id;
    @Column(name = "is_free", length = 45)
    private String isFree;
    @OneToMany(mappedBy = "placeFreeOrNot")
    private Set<Place> places;
}

