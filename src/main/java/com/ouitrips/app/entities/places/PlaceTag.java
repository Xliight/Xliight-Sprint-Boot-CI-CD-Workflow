package com.ouitrips.app.entities.places;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "Place_Tags",schema = "places")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlaceTag {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "place_tag_id_generator")
    @SequenceGenerator(name = "place_tag_id_generator", sequenceName = "places_pkid_tags_seq", allocationSize = 1,schema = "places")
    @Column(name = "pkid_place_Tags")
    private Integer id;
    @Column(name = "tag", length = 200)
    private String tag;
    @Column(name = "category", length = 200)
    private String category;

    @ManyToMany(mappedBy = "placeTags")
    private Set<Place> places;
}