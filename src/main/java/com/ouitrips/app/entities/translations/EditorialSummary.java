package com.ouitrips.app.entities.translations;


import com.ouitrips.app.entities.security.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Entity
@Table(name = "editorial_summary", schema = "translations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditorialSummary {
    @EmbeddedId
    private EditorialSummaryId id;

    @Column(name = "name", length = 400)
    private String name;
    @Column(name = "value", columnDefinition = "TEXT")
    private String value;
    @Column(name = "date_creation")
    private Date dateCreation;
    @Column(name = "date_update")
    private Date dateUpdate;

    @MapsId("fkid_lang")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_lang")
    private Language languages;

    @MapsId("fkid_object")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_object")
    private EditorialObject editorialObject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_user")
    private User user;

}
