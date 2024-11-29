package com.ouitrips.app.entities.social_network;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Post_Comments",schema = "socialNetwork")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_id_generator")
    @SequenceGenerator(name = "comment_id_generator", sequenceName = "posts_pkid_pcst_seq", allocationSize = 1,schema = "socialNetwork")
    @Column(name = "pkid_comment")
    private Integer id;
    @Column(name = "content", length = 45)
    private String content;
    @Column(name = "reaction_number", length = 45)
    private String reactionNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_post")
    private Post post;
    //Todo add relation @ManyToOne profile
}
