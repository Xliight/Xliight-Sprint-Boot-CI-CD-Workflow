package com.ouitrips.app.entities.translations;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "editorial_objects", schema = "translations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditorialObject {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "editorial_objects_id_gen")
    @SequenceGenerator(name = "editorial_objects_id_gen", sequenceName = "editorial_objects_pkid_object_seq", allocationSize = 1, schema = "translations")
    @Column(name = "pkid_object")
    private Integer id;
    @Column(name = "name", length = 45)
    private String name;
    @Column(name = "code", length = 45)
    private String code;
    @Column(name = "status")
    private Boolean status;

    @OneToMany(mappedBy = "editorialObject")
    private Set<EditorialSummary> editorialSummaries;
}
