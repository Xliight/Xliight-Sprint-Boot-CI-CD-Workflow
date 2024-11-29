package com.ouitrips.app.entities.social_network;

import com.ouitrips.app.entities.security.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
@Entity
@Table(name = "Profiles",schema = "socialNetwork")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profile_id_generator")
    @SequenceGenerator(name = "profile_id_generator", sequenceName = "profiles_pkid_profile_seq", allocationSize = 1,schema = "socialNetwork")
    @Column(name = "pkid_profile")
    private Integer id;
    @Column(name = "name", length = 45)
    private String name;

    @Column(name = "url_public", length = 45)
    private String urlPublic;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_user")
    private User user;

    @OneToMany(mappedBy = "profile")
    private List<Post> posts;

    @OneToMany(mappedBy = "followerProfile")
    private List<Follower> followers;

    @OneToMany(mappedBy = "following")
    private List<Follower> followings;

    @ManyToOne
    @JoinColumn(name = "fkid_profile_type")
    private ProfileType profileType;

    @ManyToOne
    @JoinColumn(name = "fkid_profile_privacy")
    private ProfilePrivacy profilePrivacy;


}
