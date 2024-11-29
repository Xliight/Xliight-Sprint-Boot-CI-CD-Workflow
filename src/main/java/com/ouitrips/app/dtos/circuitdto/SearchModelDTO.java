package com.ouitrips.app.dtos.circuitdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchModelDTO {
    private Long id;
    private String reference;
    private Integer order;
    private List<Integer> placesIds;
}
