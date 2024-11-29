package com.ouitrips.app.services.messagingsystemservice.sms;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Price {
    @JsonProperty("pricePerMessage")
    private double pricePerMessage;
    private String currency;
}