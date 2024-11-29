package com.ouitrips.app.entities.agency;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "agencyImages",schema="agency")
public class AgencyImages {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "agencyImages_id_agencyImages")
    @SequenceGenerator(name = "agencyImages_id_agencyImages", sequenceName = "agencyImages_pkid_agencyImages_seq", allocationSize = 1,schema = "agency")
    @Column(name ="pkid_agencyImages" )
    private Integer id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String size;

    @ManyToOne
    @JoinColumn(name = "fkid_agence", nullable = false)
    private Agency agency;

    // Getters and Setters
}