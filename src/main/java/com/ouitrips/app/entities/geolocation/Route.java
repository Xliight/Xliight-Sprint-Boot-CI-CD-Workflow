package com.ouitrips.app.entities.geolocation;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "route",schema="geolocation")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "route_id_route")
    @SequenceGenerator(name = "route_id_route", sequenceName = "route_pkid_route_seq", allocationSize = 1,schema = "geolocation")
    @Column(name ="pkid_route")
    private Integer id;

    @Embedded
    @AttributeOverride(name = "text", column = @Column(name = "distance_text"))
    @AttributeOverride(name = "value", column = @Column(name = "distance_value"))

    private RouteMetric distance;

    @Embedded
    @AttributeOverride(name = "text", column = @Column(name = "duration_text"))
    @AttributeOverride(name = "value", column = @Column(name = "duration_value"))

    private RouteMetric duration;

    @Embedded
    @AttributeOverride(name = "text", column = @Column(name = "duration_in_traffic_text"))
    @AttributeOverride(name = "value", column = @Column(name = "duration_in_traffic_value"))
    private RouteMetric durationInTraffic;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkid_requestapi")
    private RequestAPI requestAPI;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkid_metricdetails")
    private MetricDetails metricDetails;

}