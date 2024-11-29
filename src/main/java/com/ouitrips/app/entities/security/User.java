package com.ouitrips.app.entities.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ouitrips.app.entities.agency.Agency;
import com.ouitrips.app.entities.circuits.CircuitGroup;
import com.ouitrips.app.entities.social_network.Profile;
import com.ouitrips.app.entities.translations.EditorialSummary;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "users", schema = "security")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_gen")
    @SequenceGenerator(name = "users_id_gen", sequenceName = "users_pkid_user_seq", allocationSize = 1, schema = "security")
    @Column(name = "pkid_user", nullable = false)
    private Integer id;

    @Column(name = "reference")
    private String reference;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "last_login")
    private Instant lastLogin;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_name", length = 200)
    private String lastName;

    @Column(name = "first_name", length = 200)
    private String firstName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "birth_place", length = 200)
    private String birthPlace;

    @Column(name = "phone", length = 200)
    private String phone;

    @Column(name = "mobile_phone", length = 200)
    private String mobilePhone;

    @Column(name = "gsm", length = 200)
    private String gsm;

    @Column(name = "prefixe", length = 200)
    private String prefixe;

    @Column(name = "iso2", length = 200)
    private String iso2;

    @Column(name = "profession", length = 200)
    private String profession;

    @Column(name = "civility", length = 200)
    private String civility;

    @Column(name = "picture", length = 200)
    private String picture;

    @Column(name = "company_name", length = 200)
    private String companyName;

    @Column(name = "address", length = 200)
    private String address;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "additional_address", length = 200)
    private String additionalAddress;

    @Column(name = "region", length = 200)
    private String region;

    @Column(name = "city", length = 200)
    private String city;

    @Column(name = "country", length = 200)
    private String country;

    @Column(name = "token_expire_at")
    private Instant tokenExpireAt;

    @Column(name = "token_password", length = 200)
    private String tokenPassword;

    @Column(name = "token_validation", length = 200)
    private String tokenValidation;

    @Column(name = "gsm_verified")
    private Boolean gsmVerified;

    @Column(name = "email_verified")
    private Boolean emailVerified;

    @Column(name = "facebook_id", length = 200)
    private String facebookId;

    @Column(name = "google_id", length = 200)
    private String googleId;

    @Column(name = "notify_connection")
    private Boolean notifyConnection;

    @Column(name = "force_reset")
    private Boolean forceReset;

    @Column(name = "roles")
    @JdbcTypeCode(SqlTypes.JSON)
    private Object roles;

    @OneToOne(mappedBy = "user")
    private Profile profile;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<EditorialSummary> editorialSummaries;

    @OneToMany(mappedBy = "user")
    private Set<CircuitGroup> circuitGroups;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    private Agency agency;


}