package com.ouitrips.app.entities.places;
import com.ouitrips.app.entities.circuits.Location;
import com.ouitrips.app.entities.circuits.SearchModel;
import com.ouitrips.app.entities.geolocation.MetricDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "places",schema = "places")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "place_id_generator")
    @SequenceGenerator(name = "place_id_generator", sequenceName = "places_pkid_place_seq", allocationSize = 1,schema = "places")
    @Column(name = "pkid_place")
    private Integer id;
    @Column(name = "title", length = 200)
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "content")
    private String content;
    @Column(name = "latitude")
    private double latitude;
    @Column(name = "longitude")
    private double longitude;
    @Column(name = "address", length = 255)
    private String address;
    @Column(name = "popularity", length = 45)
    private String popularity;

    @OneToOne(mappedBy = "place", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PlaceInfoGeolocation placeInfoGeolocation;

    @OneToMany(mappedBy = "origin",cascade = CascadeType.ALL)  // For origin relationship
    private List<MetricDetails> originMetrics;

    @OneToMany(mappedBy = "destination",cascade = CascadeType.ALL)  // For destination relationship
    private List<MetricDetails> destinationMetrics;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private Set<PlaceRequirement> placeRequirements;
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private Set<PlacePricing> placePricing;
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private Set<PlaceInfo> placeInfos;
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private Set<PlaceOpeningHour> placeOpeningHours;
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private Set<PlaceDuration> placeDurations;
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private Set<Location> locations;

    @ManyToOne
    @JoinColumn(name = "fkid_place_Free_Or_Not")
    private PlaceFreeOrNot placeFreeOrNot;
    @ManyToOne
    @JoinColumn(name = "fkid_zone")
    private PlaceTimeZone placeTimeZone;
    @ManyToOne
    @JoinColumn(name = "fkid_city")
    private City city;


    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<LinksPt> linksPts;

    @ManyToMany
    @JoinTable(
            name = "links_tp",
            joinColumns = @JoinColumn(name = "fkid_place"),
            inverseJoinColumns = @JoinColumn(name = "fkid_place_tags")
    )
    private Set<PlaceTag> placeTags;




    @ManyToOne
    @JoinColumn(name = "search_model_id", nullable = true)
    private SearchModel searchModel;
}