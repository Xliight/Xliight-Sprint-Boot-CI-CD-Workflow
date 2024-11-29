package com.ouitrips.app.entities.geolocation;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Embeddable
public class RouteMetric {
    private String text;
    private long value;

    public RouteMetric() {}

    public RouteMetric(String text, Integer value) {
        this.text = text;
        this.value = value;
    }
}
