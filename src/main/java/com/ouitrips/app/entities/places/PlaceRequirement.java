package com.ouitrips.app.entities.places;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "place_requirements", schema = "places")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlaceRequirement {

    @EmbeddedId
    private PlaceRequirementsId id;

    @Column(name = "date_creation")
    private Date dateCreation;

    @MapsId("requirementQuestions")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_requirement_questions")
    private PlaceRequirementQuestion requirementQuestion;

    @MapsId("place")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_place")
    private Place place;


}
