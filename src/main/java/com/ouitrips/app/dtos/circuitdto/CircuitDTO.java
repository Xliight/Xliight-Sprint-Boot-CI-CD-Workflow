package com.ouitrips.app.dtos.circuitdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CircuitDTO {
    private Integer id;                      // Circuit ID
    private String name;                     // Circuit Name
    private double distance;
    private Boolean isDeleted;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Integer circuitGroupId;
    private List<Integer> agencyCircuitIds;    // IDs of AgencyCircuits
    private List<Integer> stepIds;             // IDs of Steps



}
