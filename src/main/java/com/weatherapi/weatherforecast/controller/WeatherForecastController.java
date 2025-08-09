package com.weatherapi.weatherforecast.controller;


import com.weatherapi.weatherforecast.service.WeatherForecastService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/forecast", produces = MediaType.APPLICATION_JSON_VALUE)

public class WeatherForecastController {


    private final WeatherForecastService weatherForecastService;

    public WeatherForecastController(WeatherForecastService weatherForecastService) {
        this.weatherForecastService = weatherForecastService;
    }

    @GetMapping("/geocode")
    public ResponseEntity<String> getGeocode(@RequestParam String city) {
        return ResponseEntity.ok().body(weatherForecastService.getGeocode(city).block());
    }


    @GetMapping("/current-weather")
    public ResponseEntity<String> getCurrentWeather(@RequestParam String city) {
        return ResponseEntity.ok().body(weatherForecastService.getCurrentWeather(city).block());
    }

    @GetMapping("/hourly-forecast")
    public ResponseEntity<String> getHourlyForecast(@RequestParam String city) {
        return ResponseEntity.ok().body(weatherForecastService.getHourlyForecast(city).block());
    }


    @GetMapping("/reverse-geocode")
    public ResponseEntity<String> getReverseGeocode(@RequestParam double lat, @RequestParam double lon) {
        return ResponseEntity.ok().body(weatherForecastService.getReverseGeocode(lat, lon).block());
    }

}
