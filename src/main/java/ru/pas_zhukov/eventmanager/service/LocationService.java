package ru.pas_zhukov.eventmanager.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.pas_zhukov.eventmanager.converter.LocationConverter;
import ru.pas_zhukov.eventmanager.entity.LocationEntity;
import ru.pas_zhukov.eventmanager.model.Location;
import ru.pas_zhukov.eventmanager.repository.LocationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationConverter locationConverter;

    public LocationService(LocationRepository locationRepository, LocationConverter locationConverter) {
        this.locationRepository = locationRepository;
        this.locationConverter = locationConverter;
    }

    public Location createLocation(Location location) {
        LocationEntity createdLocation =  locationRepository.save(locationConverter.toEntity(location));
        return locationConverter.toDomain(createdLocation);
    }

    public Location updateLocation(Long locationId, Location location) {
        LocationEntity locationToUpdate = locationRepository.findById(locationId).orElseThrow(() -> new EntityNotFoundException("Location with id %s not found".formatted(locationId)));
        locationToUpdate.setName(location.getName());
        locationToUpdate.setAddress(location.getAddress());
        locationToUpdate.setCapacity(location.getCapacity());
        locationToUpdate.setDescription(location.getDescription());
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
