package com.rybindev.weatherapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybindev.weatherapp.dto.SearchResultDto;
import com.rybindev.weatherapp.dto.WeatherLocationDto;
import com.rybindev.weatherapp.util.PropertiesUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
@RequiredArgsConstructor
public class WeatherService {
    private static final String API_KEY = PropertiesUtil.get("appid");
    private static final String URL = PropertiesUtil.get("url");

    private final HttpClient httpClient;

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public WeatherLocationDto getWeatherLocation(Float latitude, Float longitude) {
        String url = "%s/data/2.5/weather?lat=%4f&lon=%4f&units=metric&appid=%s".formatted(URL, latitude, longitude, API_KEY);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return objectMapper
                .readerFor(WeatherLocationDto.class)
                .readValue(response.body());
    }

    @SneakyThrows
    public SearchResultDto getWeatherLocation(String locationName) {
        String url = "%s/data/2.5/find?q=%s&units=metric&appid=%s".formatted(URL, locationName, API_KEY);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return objectMapper
                .readerFor(SearchResultDto.class)
                .readValue(response.body());
    }

}
