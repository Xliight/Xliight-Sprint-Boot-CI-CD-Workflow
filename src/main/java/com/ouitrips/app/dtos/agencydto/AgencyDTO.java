package com.ouitrips.app.dtos.agencydto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgencyDTO {
    private Integer id;              // Unique identifier of the agency
    private String name;             // Name of the agency
    private String address;          // Address of the agency
    private String contactNumber;      // Phone number of the agency
    private String email;            // Email address of the agency (if applicable)
    private String description;      // Brief description of the agency
    private Integer ownerId;         // ID of the owner (linked user)
    private Boolean isDeleted;
}
