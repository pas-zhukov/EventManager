package ru.pas_zhukov.eventmanager.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.pas_zhukov.eventmanager.converter.LocationConverter;
import ru.pas_zhukov.eventmanager.dto.request.LocationRequestDto;
import ru.pas_zhukov.eventmanager.dto.response.LocationResponseDto;
import ru.pas_zhukov.eventmanager.service.LocationService;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;
    private final LocationConverter locationConverter;

    public LocationController(LocationService locationService, LocationConverter locationConverter) {
        this.locationService = locationService;
        this.locationConverter = locationConverter;
    }

    @PostMapping
    public ResponseEntity<LocationResponseDto> createOne(@RequestBody @Valid LocationRequestDto locationToCreate) {
        LocationResponseDto createdLocation = locationConverter.toResponseDto(locationService.createLocation(locationConverter.toDomain(locationToCreate)));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLocation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationResponseDto> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(locationConverter.toResponseDto(locationService.getLocationById(id)));
    }

    @GetMapping
    public ResponseEntity<List<LocationResponseDto>> getAll() {
        return ResponseEntity.ok(locationService.getAllLocations().stream().map(locationConverter::toResponseDto).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationResponseDto> updateOne(@PathVariable Long id, @RequestBody @Valid LocationRequestDto locationToUpdate) {
        LocationResponseDto updatedLocation = locationConverter.toResponseDto(locationService.updateLocation(id, locationConverter.toDomain(locationToUpdate)));
        return ResponseEntity.ok(updatedLocation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LocationResponseDto> deleteOne(@PathVariable Long id) {
        LocationResponseDto deletedLocation = locationConverter.toResponseDto(locationService.deleteLocation(id));
        return ResponseEntity.ok(deletedLocation);
    }

}

