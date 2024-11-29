package com.ouitrips.app.entities.reviews;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "waiting_time", schema = "reviews")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WaitingTime {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "waiting_time_id_gen")
    @SequenceGenerator(name = "waiting_time_id_gen", sequenceName = "waiting_time_pkid_waiting_time_seq", allocationSize = 1, schema = "reviews")
    @Column(name = "pkid_waiting_time")
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name = "value")
    private Integer value;
    @Column(name = "order_waiting")
    private Integer order;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "recommended")
    private Boolean recommended;

    @OneToMany(mappedBy = "fkidWaitingTime")
    private Set<Review> reviews = new LinkedHashSet<>();
}
