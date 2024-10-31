package ru.pas_zhukov.eventmanager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.pas_zhukov.eventmanager.converter.LocationConverter;
import ru.pas_zhukov.eventmanager.dto.response.LocationResponseDto;
import ru.pas_zhukov.eventmanager.entity.LocationEntity;
import ru.pas_zhukov.eventmanager.repository.LocationRepository;
import ru.pas_zhukov.eventmanager.service.LocationService;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;
    private final LocationConverter locationConverter;

    private final LocationRepository locationRepository;

    public LocationController(LocationService locationService, LocationConverter locationConverter,
                              LocationRepository locationRepository) {
        this.locationService = locationService;
        this.locationConverter = locationConverter;
        this.locationRepository = locationRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationResponseDto> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(locationConverter.toResponseDto(locationService.getLocationById(id)));
    }

    @GetMapping
    public ResponseEntity<List<LocationResponseDto>> getAll() {
        return ResponseEntity.ok(locationService.getAllLocations().stream().map(locationConverter::toResponseDto).toList());
    }

}

