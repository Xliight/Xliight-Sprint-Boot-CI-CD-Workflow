package com.ouitrips.app.entities.places;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "place_infos", schema = "places")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlaceInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "info_id_generator")
    @SequenceGenerator(name = "info_id_generator", sequenceName = "place_info_pkid_info_seq", allocationSize = 1,schema = "places")
    @Column(name = "pkid_infos")
    private Integer id;
    @Column(name = "phone", length = 45)
    private String phone;
    @Column(name = "phone2", length = 45)
    private String phone2;
    @Column(name = "content")
    private String content;
    @Column(name = "web_site", length = 200)
    private String website;
    @Column(name = "address", length = 200)
    private String address;

    @ManyToOne
    @JoinColumn(name = "fkid_place")
    private Place place;
}
