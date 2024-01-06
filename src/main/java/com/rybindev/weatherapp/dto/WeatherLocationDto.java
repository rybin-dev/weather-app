package com.rybindev.weatherapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherLocationDto {
    private String name;

    private Float temperature;

    private Float latitude;

    private Float longitude;

    private String country;

    @JsonProperty("main")
    private void unpackMain(Map<String,Object> main) {
       this.temperature =  ((Number)main.get("temp")).floatValue();
    }
    @JsonProperty("coord")
    private void unpackCoord(Map<String,Object> coord) {
       this.latitude =  ((Number)coord.get("lat")).floatValue();
       this.longitude =  ((Number)coord.get("lon")).floatValue();
    }
    @JsonProperty("sys")
    private void unpackSys(Map<String,Object> sys) {
       this.country = (String) sys.get("country");
    }
}
