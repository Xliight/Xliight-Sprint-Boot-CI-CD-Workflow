package com.ouitrips.app.entities.circuits;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "category", schema = "circuits")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Category {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_id_generator")
    @SequenceGenerator(name = "category_id_generator", sequenceName = "categories_pkid_category_seq", allocationSize = 1, schema = "circuits")
    @Id
    @Column(name = "pkid_category")
    private Integer id;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "code", length = 45)
    private String code;

    @OneToMany(mappedBy = "category")
    private Set<CircuitGroup> circuitGroups;
}
