package ru.homerep.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
@Getter
@Setter
@AllArgsConstructor
public class GeoTimeRequest {
    private Long userid;
    private String startTime;
    private String endTime;
}
