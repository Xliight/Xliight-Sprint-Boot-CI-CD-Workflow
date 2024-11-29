package com.ouitrips.app.entities.circuits;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pictures",schema = "circuits")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Picture {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "picture_id_generator")
    @SequenceGenerator(name = "picture_id_generator", sequenceName = "pictures_pkid_picture_seq", allocationSize = 1, schema = "circuits")
    @Id
    @Column(name = "pkid_picture")
    private Integer id;
    @Column(name = "name", length = 45)
    private String name;
    @Column(name = "description", length = 500)
    private String description;
    @Column(name = "latitude")
    private double latitude;
    @Column(name = "longitude")
    private double longitude;

    @ManyToOne
    @JoinColumn(name = "fkid_step")
    private Step step;


}
