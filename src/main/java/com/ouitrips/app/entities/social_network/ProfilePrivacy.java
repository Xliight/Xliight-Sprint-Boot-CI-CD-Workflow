package com.ouitrips.app.entities.social_network;
import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Profile_Privacy",schema = "socialNetwork")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfilePrivacy {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profilePrivacy_id_generator")
    @SequenceGenerator(name = "profilePrivacy_id_generator", sequenceName = "profile_privacy_pkid_privacy_seq", allocationSize = 1,schema = "socialNetwork")
    @Column(name = "pkid_privacy")
    private Integer id;
    @Column(name = "name", length = 45)
    private String name;
    @Column(name = "code", length = 45)
    private String code;
}
