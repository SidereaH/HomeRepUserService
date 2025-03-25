package ru.homerep.userservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
public class GeoPair {
    private  double lat;
    private  double lng;
    private OffsetDateTime time = OffsetDateTime.now();
    public GeoPair(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }
    public GeoPair(double lat, double lng, OffsetDateTime time) {
        this.lat = lat;
        this.lng = lng;
        this.time = time;
    }
}
