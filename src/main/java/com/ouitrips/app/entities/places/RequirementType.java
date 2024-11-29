package com.ouitrips.app.entities.places;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "requirement_types", schema = "places")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequirementType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requirement_type_id_generator")
    @SequenceGenerator(name = "requirement_type_id_generator", sequenceName = "requirement_types_pkid_type_seq", allocationSize = 1,schema = "places")
    @Column(name = "pkid_type")
    private Integer id;
    @Column(name = "name", length = 45)
    private String name;
    @Column(name = "code", length = 45)
    private String code;
    @Column(name = "statut")
    private boolean statut;

    @OneToMany(mappedBy = "requirementType")
    private Set<PlaceRequirementQuestion> placeRequirementQuestions;
}
