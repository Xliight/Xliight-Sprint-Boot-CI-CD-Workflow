package com.ouitrips.app.entities.circuits;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "transport_means", schema = "circuits")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransportMeans {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transport_means_id_generator")
    @SequenceGenerator(name = "transport_means_id_generator", sequenceName = "transport_means_pkid_mode_seq", allocationSize = 1, schema = "circuits")
    @Id
    @Column(name = "pkid_mode")
    private Integer id;
    @Column(name = "name", length = 45)
    private String name;
    @Column(name = "code", length = 45)
    private String code;

    @OneToMany(mappedBy = "mode")
    private Set<LinksLs> links;
}
