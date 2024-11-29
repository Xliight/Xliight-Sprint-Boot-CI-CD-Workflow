package com.ouitrips.app.entities.reviews;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "reasons", schema = "reviews")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Reason {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reasons_id_gen")
    @SequenceGenerator(name = "reasons_id_gen", sequenceName = "reasons_pkid_reason_seq", allocationSize = 1, schema = "reviews")
    @Column(name = "pkid_reason")
    private Integer id;
    @Column(name = "object")
    private String object;
    @Column(name = "status")
    private Boolean status;

    @OneToMany(mappedBy = "fkidReason")
    private Set<Review> reviews;
}
