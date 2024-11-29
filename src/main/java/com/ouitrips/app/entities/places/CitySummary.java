package com.ouitrips.app.entities.places;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "city_summary", schema = "places")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CitySummary {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "city_summary_id_generator")
    @SequenceGenerator(name = "city_summary_id_generator", sequenceName = "city_summary_pkid_seq", allocationSize = 1, schema = "places")
    @Column(name = "pkid_city_summary")
    private Integer id;
    @Column(name = "total_places")
    private Long totalPlaces;
    @Column(name = "total_duration")
    private Long totalDuration;

    @ManyToOne
    @JoinColumn(name = "fkid_city")
    private City city;
}
