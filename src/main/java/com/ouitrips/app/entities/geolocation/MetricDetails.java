package com.ouitrips.app.entities.geolocation;
import com.ouitrips.app.entities.places.Place;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "metric_details",schema = "geolocation")
public class MetricDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "metric_id_metricDetails")
    @SequenceGenerator(name = "metric_id_metricDetails", sequenceName = "metric_details_pkid_metricdetails_seq", allocationSize = 1,schema = "geolocation")
    @Column(name ="pkid_metricdetails" )
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkid_origin", referencedColumnName = "pkid_place")
    private Place origin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkid_destination",referencedColumnName = "pkid_place")
    private Place destination;

    @OneToMany(mappedBy = "metricDetails", cascade = CascadeType.ALL, fetch = FetchType.EAGER)

    private List<Route> routes;
}