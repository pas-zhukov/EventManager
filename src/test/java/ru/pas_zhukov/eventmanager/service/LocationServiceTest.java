package ru.pas_zhukov.eventmanager.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.pas_zhukov.eventmanager.converter.LocationConverter;
import ru.pas_zhukov.eventmanager.dto.request.LocationRequestDto;
import ru.pas_zhukov.eventmanager.model.Location;

@SpringBootTest
public class LocationServiceTest {

    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationConverter locationConverter;

    @Test
    public void successOnCreateLocation() {
        LocationRequestDto locationRequestDto = new LocationRequestDto(null,
                "Hermitage",
                "Saint-Petersburg City, Dvortsovaya ploschad, 1",
                1000,
                null);
        Location createdLocation = locationService.createLocation(locationConverter.toDomain(locationRequestDto));

        Assertions.assertNotNull(createdLocation);
        Assertions.assertNotNull(createdLocation.getId());
        Assertions.assertNull(createdLocation.getDescription());

        Location createdLocationFromDB = locationService.getLocationById(createdLocation.getId());

        Assertions.assertNotNull(createdLocationFromDB);
        Assertions.assertEquals(createdLocation, createdLocationFromDB);
    }

    @Test
    public void successOnUpdateLocation() {
        LocationRequestDto locationRequestDto = new LocationRequestDto(null,
                "Hermitage",
                "Saint-Petersburg, Dvortsovaya square, 1",
                2000,
                null);
        Location createdLocation = locationService.createLocation(locationConverter.toDomain(locationRequestDto));

        LocationRequestDto updatedLocationRequestDto = new LocationRequestDto(null,
                "Kunstkamera",
                "Saint-Petersburg, Naberegnaya nevi",
                400,
                "Interesting place!");
        Location updatedLocation = locationService.updateLocation(createdLocation.getId(), locationConverter.toDomain(updatedLocationRequestDto));

        Assertions.assertNotNull(updatedLocation);

        Assertions.assertEquals(createdLocation.getId(), updatedLocation.getId());
        Assertions.assertEquals("Kunstkamera", updatedLocation.getName());
        Assertions.assertEquals("Saint-Petersburg, Naberegnaya nevi", updatedLocation.getAddress());
        Assertions.assertEquals(400, updatedLocation.getCapacity());
        Assertions.assertEquals("Interesting place!", updatedLocation.getDescription());

        Assertions.assertNotEquals(createdLocation.getDescription(), updatedLocation.getDescription());
        Assertions.assertNotEquals(createdLocation.getName(), updatedLocation.getName());
        Assertions.assertNotEquals(createdLocation.getCapacity(), updatedLocation.getCapacity());
        Assertions.assertNotEquals(createdLocation.getAddress(), updatedLocation.getAddress());
    }

    @Test
    public void successOnDeleteLocation() {
        LocationRequestDto locationRequestDto = new LocationRequestDto(null,
                "Hermitage",
                "Saint-Petersburg, Dvortsovaya square, 1",
                2000,
                "null");
        Location createdLocation = locationService.createLocation(locationConverter.toDomain(locationRequestDto));
        locationService.deleteLocation(createdLocation.getId());
    }
}