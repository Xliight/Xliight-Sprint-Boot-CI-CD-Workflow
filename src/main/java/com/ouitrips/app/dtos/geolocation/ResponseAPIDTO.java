package com.ouitrips.app.dtos.geolocation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseAPIDTO {

    private Integer id;
    private String jsonResponse;
    private Integer requestAPIId; // Include the RequestAPI ID
}