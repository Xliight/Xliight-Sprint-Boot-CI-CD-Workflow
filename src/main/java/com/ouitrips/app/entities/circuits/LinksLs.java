package com.ouitrips.app.entities.circuits;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "links_ls",schema = "circuits")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LinksLs {

    @EmbeddedId
    private LinksLsId id;

    @Column(name = "duration")
    private float duration;
    @Column(name = "distance")
    private float distance;

    @MapsId("fkidLocationStart")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_location_start")
    private Location locationStart;

    @MapsId("fkidLocationEnd")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_location_end")
    private Location locationEnd;

    @MapsId("fkidStep")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_step")
    private Step step;

    @MapsId("fkidMode")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_mode")
    private TransportMeans mode;
}
