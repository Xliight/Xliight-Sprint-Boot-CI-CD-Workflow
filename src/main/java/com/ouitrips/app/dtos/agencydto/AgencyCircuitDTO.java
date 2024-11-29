package com.ouitrips.app.dtos.agencydto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgencyCircuitDTO {
    private Integer id;
    private Integer agencyId;
    private Integer circuitId;  // Referencing the circuit ID instead of the whole Circuit entity
    private String description;
}