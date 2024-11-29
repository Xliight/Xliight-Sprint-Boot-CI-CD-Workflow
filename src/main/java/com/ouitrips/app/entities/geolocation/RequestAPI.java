package com.ouitrips.app.entities.geolocation;

import lombok.*;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "request_api",schema="geolocation")
public class RequestAPI {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "request_id_requestapi")
    @SequenceGenerator(name = "request_id_requestapi", sequenceName = "request_api_pkid_request_seq", allocationSize = 1,schema = "geolocation")
    @Column(name ="pkid_request" )
    private Integer id;
    @Column(name = "origin_coords")
    private String originCoords;
    @Column(name = "destination_coords")
    private String destinationCoords;
    @Column(name = "mode")
    private String mode;
    @Column(name = "language")
    private String language;
    @Column(name = "route_restriction")
    private String routeRestriction;
    @Column(name = "departure_date")
    private Instant departureDate;
    @Column(name = "arrivaltime")
    private Instant arrivalTime;
    @Column(name = "traffic_model")
    private String trafficModel;
    @Column(name = "traffic_transit")
    private String trafficTransit;
    @Column(name = "transit_routing_preference")
    private String transitRoutingPreference;
    @Column(name = "unit")
    private String unit;
    @OneToMany(mappedBy = "requestAPI", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ResponseAPI> responses;
    @OneToMany(mappedBy = "requestAPI", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Route> routes;


}
