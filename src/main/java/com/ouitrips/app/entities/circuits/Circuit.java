package com.ouitrips.app.entities.circuits;
import com.ouitrips.app.entities.agency.AgencyCircuit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Entity
@Table(name = "Circuits", schema = "circuits")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Circuit {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "circuit_id_generator")
    @SequenceGenerator(name = "circuit_id_generator", sequenceName = "circuits_pkid_circuit_seq", allocationSize = 1, schema = "circuits")
    @Id
    @Column(name = "pkid_circuit")
    private Integer id;
    @Column(name = "name", length = 45)
    private String name;
    @Column(name = "distance")
    private double distance;
    @Column(name = "is_delete")
    private Boolean isDeleted;
    @Column(name = "date_debut")
    private LocalDate dateDebut;
    @Column(name = "date_fin")
    private LocalDate dateFin;

    @OneToMany(mappedBy = "circuit")
    private Set<Step> steps;

    @ManyToOne
    @JoinColumn(name = "fkid_circuit_group")
    private CircuitGroup circuitGroup;

    @OneToMany(mappedBy = "circuit", cascade = CascadeType.ALL)
    private List<AgencyCircuit> agencyCircuits = new ArrayList<>();



}
