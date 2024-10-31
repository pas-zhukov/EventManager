package ru.pas_zhukov.eventmanager.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.pas_zhukov.eventmanager.converter.LocationConverter;
import ru.pas_zhukov.eventmanager.entity.LocationEntity;
import ru.pas_zhukov.eventmanager.model.Location;
import ru.pas_zhukov.eventmanager.repository.LocationRepository;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationConverter locationConverter;

    public LocationService(LocationRepository locationRepository, LocationConverter locationConverter) {
        this.locationRepository = locationRepository;
        this.locationConverter = locationConverter;
    }

    @Transactional
    public Location createLocation(Location location) {
        LocationEntity createdLocation =  locationRepository.save(locationConverter.toEntity(location));
        return locationConverter.toDomain(createdLocation);
    }

    @Transactional
    public Location updateLocation(Long locationId, Location location) {
        LocationEntity updatedLocation = locationRepository.findById(locationId).orElseThrow(EntityNotFoundException::new);
        updatedLocation.setName(location.getName());
        updatedLocation.setAddress(location.getAddress());
        updatedLocation.setCapacity(location.getCapacity());
        updatedLocation.setDescription(location.getDescription());
        locationRepository.save(updatedLocation);
        return locationConverter.toDomain(updatedLocation);
    }

    @Transactional
    public void deleteLocation(Long locationId) {
        locationRepository.deleteById(locationId);
    }

    @Transactional
    public Location getLocationById(Long locationId) {
        LocationEntity location = locationRepository.findById(locationId).orElseThrow(EntityNotFoundException::new);
        return locationConverter.toDomain(location);
    }
}
