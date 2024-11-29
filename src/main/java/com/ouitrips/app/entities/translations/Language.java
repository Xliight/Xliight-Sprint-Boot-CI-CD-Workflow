package com.ouitrips.app.entities.translations;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "languages", schema = "translations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "languages_id_gen")
    @SequenceGenerator(name = "languages_id_gen", sequenceName = "languages_pkid_lang_seq", allocationSize = 1, schema = "translations")
    @Column(name = "pkid_lang")
    private Integer id;
    @Column(name = "name", length = 45)
    private String name;
    @Column(name = "code", length = 45)
    private String code;

    @OneToMany(mappedBy = "languages")
    private Set<EditorialSummary> editorialSummaries;
}
