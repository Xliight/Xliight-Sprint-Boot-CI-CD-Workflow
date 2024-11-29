package com.ouitrips.app.dtos.circuitdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StepDTO {
    private Integer id;
    private String name;
    private String directions;
    private Integer orderStep;
    private Boolean state;

    // If you need to include pictures, links, or tracking circuits, you can add those as well
    // private List<PictureDTO> pictures; // Example if you have a PictureDTO
    // private List<LinkDTO> links; // Example if you have a LinkDTO
    // private List<TrackingCircuitDTO> trackingCircuits; // Example if you have a TrackingCircuitDTO
}