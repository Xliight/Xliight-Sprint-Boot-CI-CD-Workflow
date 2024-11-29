package com.ouitrips.app.entities.social_network;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "Followers",schema = "socialNetwork")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Follower {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "follower_id_generator")
    @SequenceGenerator(name = "follower_id_generator", sequenceName = "followers_pkid_follower_seq", allocationSize = 1,schema = "socialNetwork")
    @Column(name = "pkid_follower")
    private Integer id;
    @Column(name = "date_follow")
    private Date dateFollow;

    @Column(name = "date_unfollow")
    private Date dateUnfollow;

    @Column(name = "is_approved")
    private boolean isApproved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_follower")
    private Profile followerProfile;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_following")
    private Profile following;


}
