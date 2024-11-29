package com.ouitrips.app.entities.social_network;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Entity
@Table(name = "Reaction_Levels",schema = "socialNetwork")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReactionLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reactionLevel_id_generator")
    @SequenceGenerator(name = "reactionLevel_id_generator", sequenceName = "reactionLevel_pkid_reactionLevels_seq", allocationSize = 1,schema = "socialNetwork")
    @Column(name = "pkid_level")
    private Integer id;
    @Column(name = "value")
    private int value;
    @Column(name = "order_level")
    private int order;

    @OneToMany(mappedBy = "reactionLevel")
    private List<Reaction> reactions;
}
