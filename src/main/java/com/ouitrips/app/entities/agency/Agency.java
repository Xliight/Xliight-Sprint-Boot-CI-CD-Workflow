package com.ouitrips.app.entities.agency;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ouitrips.app.entities.security.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "agency",schema="agency")
public class Agency {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "agency_id_agency")
    @SequenceGenerator(name = "agency_id_agency", sequenceName = "agency_pkid_agency_seq", allocationSize = 1,schema = "agency")
    @Column(name ="pkid_agency" )
    private Integer id;

    @Column(name = "ICE", length = 20)
    private String ice;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "contactNumber", length = 15)
    private String contactNumber;

    @Column(name = "address", length = 100)
    private String address;


    @Column(name = "logo")
    private String logo;  // URL or file path for the agency's logo

    @Column(name = "description" ,length = 500)
    private String description;  // Short description or overview of the agency


    @Column
    private LocalDate openingDate;

    @Column
    private LocalDate closingDate;

    @Column
    private LocalTime opensHours;

    @Column
    private LocalTime closedHours;
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "agency", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AgencyCircuit> agencyCircuits = new ArrayList<>();

    @OneToMany(mappedBy = "agency", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AgencyImages> images;

    @OneToMany(mappedBy = "agency", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Document> documents;

    @Column(nullable = false)
    private Boolean isVerified = false;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Column(nullable = false)
    private Boolean isEnable = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;
}
