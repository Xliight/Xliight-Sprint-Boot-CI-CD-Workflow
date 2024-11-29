package com.ouitrips.app.entities.circuits;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "search_circuit", schema = "circuits")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchCircuit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "earchcircuit_id_searchcircuit")
    @SequenceGenerator(name = "searchcircuit_id_searchcircuit", sequenceName = "searchcircuit_pkid_searchcircuit_seq", allocationSize = 1,schema = "circuits")
    @Column(name ="pkid_searchcircuit" )
    private Integer id;
    @Column(name = "name", length = 45)
    private String name;
    @Column(name = "reference",unique = true,nullable = false)
    private String reference;
    @Column(name = "distance")
    private double distance;
    @Column(name = "is_delete")
    private Boolean isDeleted;
    @Column(name = "date_debut")
    private LocalDate dateDebut;
    @Column(name = "date_fin")
    private LocalDate dateFin;


    @OneToMany(mappedBy = "searchCircuit", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SearchModel> searchModels = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}


