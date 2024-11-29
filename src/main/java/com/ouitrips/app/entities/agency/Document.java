package com.ouitrips.app.entities.agency;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.*;
import jakarta.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "document",schema="agency")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_id_document")
    @SequenceGenerator(name = "document_id_document", sequenceName = "document_pkid_document_seq", allocationSize = 1,schema = "agency")
    @Column(name ="pkid_document" )
    private Integer id;

    @Column(name = "title", length = 50)
    private String title;

    @Column(name = "type", length = 20)
    private String type;

    @Column(name = "filePath", length = 255)
    private String filePath;

    @Lob
    @Column(name = "file")
    private byte[] file;

    @ManyToOne
    @JoinColumn(name = "fkid_agency")
    private Agency agency;

    // Getters and Setters
}

