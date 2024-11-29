package com.ouitrips.app.entities.places;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "place_durations", schema = "places")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlaceDuration {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "duration_id_generator")
    @SequenceGenerator(name = "duration_id_generator", sequenceName = "place_durations_pkid_step_seq", allocationSize = 1,schema = "places")
    @Column(name = "pkid_duration")
    private Integer id;


    @Column(name = "duration")
    private Long duration;

    @ManyToOne
    @JoinColumn(name = "fkid_place")
    private Place place;
}
