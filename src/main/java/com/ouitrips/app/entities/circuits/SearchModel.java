package com.ouitrips.app.entities.circuits;

import com.ouitrips.app.entities.places.Place;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "search_model",schema="circuits")
public class SearchModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "searchmodel_id_searchmodel")
    @SequenceGenerator(name = "searchmodel_id_searchmodel", sequenceName = "searchmodel_pkid_searchmodel_seq", allocationSize = 1,schema = "circuits")
    @Column(name ="pkid_searchmodel" )
    private Integer id;

    @Column(name = "orderPlace")
    private Integer order;

    @Column(name = "reference",unique = true)
    private String reference;

    @OneToMany(mappedBy = "searchModel", cascade = CascadeType.ALL, orphanRemoval = false,fetch = FetchType.EAGER)
    private List<Place> places = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "fkid_search_circuit")
    private SearchCircuit searchCircuit;
}
