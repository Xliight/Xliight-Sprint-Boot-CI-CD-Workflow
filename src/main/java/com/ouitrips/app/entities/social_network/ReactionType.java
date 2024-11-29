package com.ouitrips.app.entities.social_network;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Entity
@Table(name = "Reaction_Types",schema = "socialNetwork")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReactionType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reaction_type_id_generator")
    @SequenceGenerator(name = "reaction_type_id_generator", sequenceName = "reaction_types_pkid_type_seq", allocationSize = 1,schema = "socialNetwork")
    @Column(name = "pkid_type")
    private Integer id;
    @Column(name = "name", length = 45)
    private String name;
    @Column(name = "description", length = 45)
    private String description;
    @Column(name = "code", length = 45)
    private String code;
    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "reactionType")
    private List<Reaction> reactions;
}
