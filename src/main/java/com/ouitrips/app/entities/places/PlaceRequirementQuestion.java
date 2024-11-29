package com.ouitrips.app.entities.places;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "places_requirement_questions", schema = "places")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlaceRequirementQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requirement_question_id_generator")
    @SequenceGenerator(name = "requirement_question_id_generator", sequenceName = "places_requirement_questions_pkid_place_seq", allocationSize = 1,schema = "places")
    @Column(name = "pkid_requirement_questions")
    private Integer id;
    @Column(name = "name", length = 45)
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "status", length = 45)
    private String status;

    @OneToMany(mappedBy = "requirementQuestion")
    private Set<PlaceRequirement> placeRequirements;

    @ManyToOne
    @JoinColumn(name = "fkid_type")
    private RequirementType requirementType;





}
