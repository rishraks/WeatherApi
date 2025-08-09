package com.weatherapi.weatherforecast.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WeatherForecastService {

    @Value("${spring.app.apiKey}")
    private String API_KEY;

    @Value("${weather.api.geocoding.url}")
    private String GEOCODING_BASE_URL;

    @Value("${weather.api.geo.url}")
    private String REVERSE_GEOCODING_BASE_URL;

    @Value("${weather.api.weather.url}")
    private String WEATHER_BASE_URL;

    @Value("${weather.api.hourly.url}")
    private String WEATHER_HOURLY_FORECAST_BASE_URL;

    private final WebClient.Builder webClientBuilder;

    public WeatherForecastService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<String> getGeocode(String city) {
        return webClientBuilder.baseUrl(GEOCODING_BASE_URL).build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("q", city)
                        .queryParam("appid", API_KEY)
                        .queryParam("limit", 1)
                        .queryParam("units", "metric")
                        .queryParam("lang", "en")
                        .build())

                .retrieve()
                .bodyToMono(String.class);

    }

    public Mono<String> getCurrentWeather(String city) {
        return getGeocode(city)
                .flatMap(geocode -> {
                    JsonNode jsonNode;
                    try {
                        jsonNode = new ObjectMapper().readTree(geocode);
                        if (jsonNode.isEmpty()) {
                            return Mono.error(new RuntimeException("City not found"));
                        }
                        double lat = jsonNode.get(0).get("lat").asDouble();
                        double lon = jsonNode.get(0).get("lon").asDouble();

                        return webClientBuilder.baseUrl(WEATHER_BASE_URL).build()
                                .get()
                                .uri(uriBuilder -> uriBuilder
                                        .queryParam("lat", lat)
                                        .queryParam("lon", lon)
                                        .queryParam("appid", API_KEY)
                                        .queryParam("units", "metric")
                                        .queryParam("lang", "en")
                                        .build())
                                .retrieve()
                                .bodyToMono(String.class);
                    } catch (JsonProcessingException e) {
                        return Mono.error(new RuntimeException(e));
                    }
                });
    }

    public Mono<String> getHourlyForecast(String city) {
        return getGeocode(city)
                .flatMap(geocode -> {
                    JsonNode jsonNode;
                    try {
                        jsonNode = new ObjectMapper().readTree(geocode);
                        if (jsonNode.isEmpty()) {
                            return Mono.error(new RuntimeException("City not found"));
                        }
                        double lat = jsonNode.get(0).get("lat").asDouble();
                        double lon = jsonNode.get(0).get("lon").asDouble();

                        return webClientBuilder.baseUrl(WEATHER_HOURLY_FORECAST_BASE_URL).build()
                                .get()
                                .uri(uriBuilder -> uriBuilder
                                        .queryParam("lat", lat)
                                        .queryParam("lon", lon)
                                        .queryParam("appid", API_KEY)
                                        .queryParam("units", "metric")
                                        .queryParam("lang", "en")
                                        .build())
                                .retrieve()
                                .bodyToMono(String.class);
                    } catch (JsonProcessingException e) {
                        return Mono.error(new RuntimeException(e));
                    }
                });
    }

    public Mono<String> getReverseGeocode(double lat, double lon) {
        return webClientBuilder.baseUrl(REVERSE_GEOCODING_BASE_URL).build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("appid", API_KEY)
                        .queryParam("units", "metric")
                        .queryParam("lang", "en")
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}
