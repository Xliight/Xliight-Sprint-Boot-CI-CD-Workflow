package com.ouitrips.app.entities.agency;

import com.ouitrips.app.entities.security.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "agency_history", schema = "agency")
public class AgencyHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "agencyhistory_id_agencyhistory")
    @SequenceGenerator(name = "agencyhistory_id_agencyhistory", sequenceName = "agency_history_pkid_agencyhistory_seq", allocationSize = 1, schema = "agency")
    @Column(name = "pkid_agencyHistory")
    private Integer id;

    @Column(name = "trip_date")
    private LocalDateTime tripDate;  // Date of the trip

    @Column(name = "status", length = 50)
    private String status;  // Status of the trip (e.g., Completed, Canceled)

    @Column(name = "notes", length = 500)
    private String notes;  // Additional notes about the trip

    @ManyToOne
    @JoinColumn(name = "fkid_agencyCircuit")
    private AgencyCircuit agencyCircuit;  // Linking to AgencyCircuit

    @ManyToOne
    @JoinColumn(name = "fkid_user")
    private User madeBy;
}