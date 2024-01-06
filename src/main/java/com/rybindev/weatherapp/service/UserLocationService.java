package com.rybindev.weatherapp.service;

import com.rybindev.weatherapp.dto.UserLocationDto;
import com.rybindev.weatherapp.dto.WeatherLocationDto;
import com.rybindev.weatherapp.entity.Location;
import com.rybindev.weatherapp.entity.User;
import com.rybindev.weatherapp.mapper.LocationMapper;
import com.rybindev.weatherapp.repository.LocationRepository;
import com.rybindev.weatherapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
public class UserLocationService {
    private final UserRepository userRepository;

    private final LocationRepository locationRepository;

    private final LocationMapper locationMapper;

    private final WeatherService weatherService;

    @Transactional
    public void addUserLocation(Long userId, Float latitude, Float longitude) {
        Location location = findLocation(latitude, longitude);

        userRepository.findById(userId).ifPresent(user -> user.getLocations().add(location));
    }

    private Location findLocation(Float latitude, Float longitude) {
        Location location = locationRepository.findByCoordinates(latitude, longitude).orElse(null);

        if (location != null) {
            if (location.isExpired()) {
                updateLocation(location);
            }
            return location;
        }

        WeatherLocationDto wl = weatherService.getWeatherLocation(latitude, longitude);

        location = locationMapper.mapFrom(wl);

        return locationRepository.save(location);
    }

    private Location updateLocation(Location location) {
        WeatherLocationDto wl = weatherService.getWeatherLocation(location.getLatitude(), location.getLongitude());

        location.setTemperature(wl.getTemperature());
        location.setUpdatedAt(Instant.now());

        return locationRepository.save(location);
    }


    @Transactional
    public void deleteUserLocation(Long userId, Long locationId) {
        userRepository.findById(userId)
                .map(User::getLocations)
                .get()
                .removeIf(location -> location.getId().equals(locationId));
    }

    @Transactional
    public List<UserLocationDto> getUserLocation(Long userId) {
        return userRepository.findById(userId)
                .map(User::getLocations)
                .get()
                .stream()
                .map(location -> {
                    if (location.isExpired()) {
                        updateLocation(location);
                    }

                    return UserLocationDto.builder()
                            .id(location.getId())
                            .name(location.getName())
                            .temperature(location.getTemperature())
                            .latitude(location.getLatitude())
                            .longitude(location.getLatitude())
                            .country(location.getCountry())
                            .build();
                })
                .toList();
    }

}
