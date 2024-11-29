package com.ouitrips.app.entities.agency;

import com.ouitrips.app.entities.circuits.Circuit;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.*;
import jakarta.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "agency_circuit",schema="agency")
public class AgencyCircuit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aagencycircuit_id_agencycircuit")
    @SequenceGenerator(name = "agencycircuit_id_agencycircuit", sequenceName = "agency_circuit_pkid_agencycircuit_seq", allocationSize = 1,schema = "agency")
    @Column(name ="pkid_agencyCircuit" )
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fkid_agency")
    private Agency agency;

    @ManyToOne
    @JoinColumn(name = "fkid_circuit")
    private Circuit circuit;

    @Column(name = "description", length = 255)
    private String description;

}