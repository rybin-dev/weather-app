package com.rybindev.weatherapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLocationDto {
    private Long id;

    private String name;

    private Float temperature;

    private Float latitude;

    private Float longitude;

    private String country;
}
