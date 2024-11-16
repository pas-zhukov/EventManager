package ru.pas_zhukov.eventmanager.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ru.pas_zhukov.eventmanager.converter.LocationConverter;
import ru.pas_zhukov.eventmanager.entity.LocationEntity;
import ru.pas_zhukov.eventmanager.model.Location;
import ru.pas_zhukov.eventmanager.repository.LocationRepository;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationConverter locationConverter;

    public LocationService(LocationRepository locationRepository, LocationConverter locationConverter) {
        this.locationRepository = locationRepository;
        this.locationConverter = locationConverter;
    }

    public Location createLocation(Location location) {
        LocationEntity createdLocation = locationRepository.save(locationConverter.toEntity(location));
        return locationConverter.toDomain(createdLocation);
    }

    public Location updateLocation(Long locationId, Location location) {
        if (!locationRepository.existsById(locationId)) {
            throw new EntityNotFoundException("Location with id %s not found".formatted(locationId));
        }
        LocationEntity locationToUpdate = locationConverter.toEntity(location);
        locationToUpdate.setId(locationId);
        locationRepository.save(locationToUpdate);
        return locationConverter.toDomain(locationToUpdate);
    }

    public Location deleteLocation(Long locationId) {
        LocationEntity location = locationRepository.findById(locationId).orElseThrow(() -> new EntityNotFoundException("Location with id %s not found".formatted(locationId)));
        locationRepository.deleteById(locationId);
        return locationConverter.toDomain(location);
    }

    public Location getLocationById(Long locationId) {
        LocationEntity location = locationRepository.findById(locationId).orElseThrow(() -> new EntityNotFoundException("Location with id %s not found".formatted(locationId)));
        return locationConverter.toDomain(location);
    }

    public List<Location> getAllLocations() {
        List<LocationEntity> locationEntities = locationRepository.findAll();
        return locationEntities.stream().map(locationConverter::toDomain).toList();
    }
}
