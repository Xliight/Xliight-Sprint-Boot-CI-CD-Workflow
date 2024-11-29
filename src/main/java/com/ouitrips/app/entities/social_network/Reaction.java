package com.ouitrips.app.entities.social_network;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "Reactions",schema = "socialNetwork")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reaction_id_generator")
    @SequenceGenerator(name = "reaction_id_generator", sequenceName = "reactions_pkid_reaction_seq", allocationSize = 1,schema = "socialNetwork")
    @Column(name = "pkid_reaction")
    private Integer id;
    @Column(name = "date_reaction", length = 45)
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_post")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_profile")
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_type")
    private ReactionType reactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_level")
    private ReactionLevel reactionLevel;
}
