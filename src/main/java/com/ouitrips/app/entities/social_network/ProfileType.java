package com.ouitrips.app.entities.social_network;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Entity
@Table(name = "Profile_Types",schema = "socialNetwork")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfileType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profile_type_id_generator")
    @SequenceGenerator(name = "profile_type_id_generator", sequenceName = "profile_types_pkid_type_seq", allocationSize = 1,schema = "socialNetwork")
    @Column(name = "pkid_type")
    private Integer id;
    @Column(name = "name", length = 45)
    private String name;
    @Column(name = "code", length = 45)
    private String code;
    @Column(name = "status", length = 45)
    private String status;

    @OneToMany(mappedBy = "profileType")
    private List<Profile> profiles;
}
