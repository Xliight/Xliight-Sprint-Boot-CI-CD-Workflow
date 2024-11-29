package com.ouitrips.app.entities.circuits;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "Tracking_Circuit", schema = "circuits")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrackingCircuit {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tracking_id_generator")
    @SequenceGenerator(name = "tracking_id_generator", sequenceName = "tracking_pkid_circuit_seq", allocationSize = 1, schema = "circuits")
    @Id
    @Column(name = "pkid_tracking")
    private Integer id;
    @Column(name = "start_date")
    private Instant startDate;
    @Column(name = "updated_date")
    private Instant updatedDate;
    @Column(name = "longitude")
    private String longitude;
    @Column(name = "latitude")
    private String latitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_circuit")
    private Circuit circuit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_current_step")
    private Step step;
}
