package com.ouitrips.app.entities.social_network;

import com.ouitrips.app.entities.circuits.Circuit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "Post",schema = "socialNetwork")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_id_generator")
    @SequenceGenerator(name = "post_id_generator", sequenceName = "posts_pkid_post_seq", allocationSize = 1,schema = "socialNetwork")
    @Column(name = "pkid_post")
    private Integer id;
    @Column(name = "post_date")
    private Instant postDate;
    @Column(name = "description", length = 500)
    private String description;
    @Column(name = "content")
    private String content;
    @Column(name = "picture")
    private String picture;
    @Column(name = "style")
    private String style;
    @Column(name = "visibility")
    private boolean visibility;

    @OneToMany(mappedBy = "post")
    private Set<PostComment> comments;
    @OneToMany(mappedBy = "post")
    private Set<Reaction> reactions;

    @ManyToOne
    @JoinColumn(name = "fkid_circuit")
    private Circuit circuit;
    @ManyToOne
    @JoinColumn(name = "fkid_profile")
    private Profile profile;

    @ManyToMany
    @JoinTable(
            name = "links_pt",
            joinColumns = @JoinColumn(name = "fkid_post"),
            inverseJoinColumns = @JoinColumn(name = "fkid_tag")
    )
    private Set<Tag> tags;
}
