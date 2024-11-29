package com.ouitrips.app.dtos.circuitdto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchCircuitDTO {
    private Integer id;
    private String name;
    private String reference;
    private double distance;
    private Boolean isDeleted;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    // Only IDs for related entities
    private Integer circuitGroupId;
    private List<Integer> stepIds;
    private List<Integer> searchModelIds;
}