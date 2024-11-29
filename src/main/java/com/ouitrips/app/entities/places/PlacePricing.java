package com.ouitrips.app.entities.places;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "place_pricing", schema = "places")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlacePricing {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pricing_id_generator")
    @SequenceGenerator(name = "pricing_id_generator", sequenceName = "place_pricing_pkid_pricing_seq", allocationSize = 1,schema = "places")
    @Column(name = "pkid_pricing")
    private Integer id;
    @Column(name = "price_min")
    private Double priceMin;
    @Column(name = "price_max")
    private Double priceMax;
    @Column(name = "price")
    private Double price;

    @ManyToOne
    @JoinColumn(name = "fkid_place")
    private Place place;
}
