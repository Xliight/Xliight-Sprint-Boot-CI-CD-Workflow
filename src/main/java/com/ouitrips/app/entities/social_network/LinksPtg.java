package com.ouitrips.app.entities.social_network;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Links_ptg",schema = "socialNetwork")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LinksPtg  {
    @SequenceGenerator(name = "links_ptg_id_generator", sequenceName = "links_ptg_pkid_link_ptg_seq", allocationSize = 1,schema = "socialNetwork")
    @EmbeddedId
    private LinksPtgId id;

    @MapsId("Post")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_Post")
    private Post post;

    @MapsId("Tag")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_Tag")
    private Tag tag;
}
