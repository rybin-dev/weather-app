package com.rybindev.weatherapp.mapper;

import com.rybindev.weatherapp.dto.WeatherLocationDto;
import com.rybindev.weatherapp.entity.Location;

public class LocationMapper implements Mapper<WeatherLocationDto, Location> {
    @Override
    public Location mapFrom(WeatherLocationDto wl) {
        return Location.builder()
                .name(wl.getName())
                .latitude(wl.getLatitude())
                .longitude(wl.getLongitude())
                .temperature(wl.getTemperature())
                .country(wl.getCountry())
                .build();
    }
}
