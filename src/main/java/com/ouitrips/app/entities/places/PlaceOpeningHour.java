package com.ouitrips.app.entities.places;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "place_opening_hours", schema = "places")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlaceOpeningHour {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "opening_hour_id_generator")
    @SequenceGenerator(name = "opening_hour_id_generator", sequenceName = "place_opening_hours_pkid_place_seq", allocationSize = 1,schema = "places")
    @Column(name = "pkid_opening_hour")
    private Integer id;
    @Column(name = "opening_time")
    private LocalTime openingTime;
    @Column(name = "close_time")
    private LocalTime closeTime;

    @ManyToOne
    @JoinColumn(name = "fkid_place")
    private Place place;
}
