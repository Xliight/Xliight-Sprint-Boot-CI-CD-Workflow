package com.ouitrips.app.entities.social_network;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "Tags",schema = "socialNetwork")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_id_generator")
    @SequenceGenerator(name = "tag_id_generator", sequenceName = "tags_pkid_tag_seq", allocationSize = 1,schema = "socialNetwork")
    @Column(name = "pkid_tag")
    private Integer id;
    @Column(name = "name", length = 450)
    private String name;
    @Column(name = "reference", length = 450)
    private String reference;

    @ManyToMany(mappedBy = "tags")
    private Set<Post> posts;
}
