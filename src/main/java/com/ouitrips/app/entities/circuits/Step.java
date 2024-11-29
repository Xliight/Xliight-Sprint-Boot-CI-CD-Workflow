package com.ouitrips.app.entities.circuits;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "steps",schema = "circuits")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Step {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "step_id_generator")
    @SequenceGenerator(name = "step_id_generator", sequenceName = "steps_pkid_step_seq", allocationSize = 1,schema = "circuits")
    @Id
    @Column(name = "pkid_step")
    private Integer id;
    @Column(name = "name", length = 45)
    private String name;
    @Column(name = "directions")
    private String directions;
    @Column(name = "order_step")
    private Integer orderStep;
    @Column(name = "state")
    private Boolean state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_circuit")
    private Circuit circuit;



    @OneToMany(mappedBy = "step")
    private Set<Picture> pictures;
    @OneToMany(mappedBy = "step")
    private Set<LinksLs> links;
    @OneToMany(mappedBy = "step")
    private Set<TrackingCircuit> trackingCircuits;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "fkid_circuitSearch")
//    private SearchCircuit circuitSearch;
}