package ru.pas_zhukov.eventmanager.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import ru.pas_zhukov.eventmanager.TestInContainer;
import ru.pas_zhukov.eventmanager.converter.LocationConverter;
import ru.pas_zhukov.eventmanager.dto.request.LocationRequestDto;
import ru.pas_zhukov.eventmanager.model.Location;
import ru.pas_zhukov.eventmanager.service.LocationService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class LocationControllerTest extends TestInContainer {

    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationConverter locationConverter;

    @Test
    public void shouldSuccessOnCreateLocation() throws Exception {
        LocationRequestDto locationRequestDto = new LocationRequestDto(null,
                "Hermitage",
                "Saint-Petersburg City, Dvortsovaya ploschad, 1",
                1000,
                "some description");
        String locationJson = jacksonObjectMapper.writeValueAsString(locationRequestDto);
        String responseJson = mockMvc.perform(post("/locations").contentType(MediaType.APPLICATION_JSON).content(locationJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Location createdLocation = jacksonObjectMapper.readValue(responseJson, Location.class);

        Assertions.assertNotNull(createdLocation);
        Assertions.assertNotNull(createdLocation.getId());
        Assertions.assertEquals(locationRequestDto.getName(), createdLocation.getName());
        Assertions.assertEquals(locationRequestDto.getAddress(), createdLocation.getAddress());
        Assertions.assertEquals(locationRequestDto.getCapacity(), createdLocation.getCapacity());
        Assertions.assertEquals(locationRequestDto.getDescription(), createdLocation.getDescription());
    }

    @Test
    public void shouldNotCreateLocationOnInvalidRequest() throws Exception {
        LocationRequestDto locationRequestDto = new LocationRequestDto(10L,
                "",
                "",
                -600,
                null);
        String locationJson = jacksonObjectMapper.writeValueAsString(locationRequestDto);
        mockMvc.perform(post("/locations").contentType(MediaType.APPLICATION_JSON).content(locationJson))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void successOnGetLocationById() throws Exception {
        LocationRequestDto locationRequestDto = new LocationRequestDto(null,
                "Hermitage",
                "Saint-Petersburg City, Dvortsovaya ploschad, 1",
                1000,
                null);
        Location createdLocation = locationService.createLocation(locationConverter.toDomain(locationRequestDto));

        String gotLocationJson = mockMvc.perform(get("/locations/{id}", createdLocation.getId()))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Location gotLocation = jacksonObjectMapper.readValue(gotLocationJson, Location.class);

        org.assertj.core.api.Assertions.assertThat(createdLocation).usingRecursiveComparison().isEqualTo(gotLocation);
    }

    @Test
    public void shouldReturnNotFoundOnGetLocationByNotExistingId() throws Exception {
        mockMvc.perform(get("/locations/{id}", Long.MAX_VALUE))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void shouldSuccessOnUpdateLocation() throws Exception {
        Location sourceLocation = new Location(null,
                "Hermitage",
                "Saint-Petersburg City, Dvortsovaya ploschad, 1",
                1000,
                null);
        Location createdLocation = locationService.createLocation(sourceLocation);

        LocationRequestDto locationToUpdate = new LocationRequestDto(null,
                "Gazprom Arena",
                "Sportivanaya metro station",
                10000,
                "super place");
        String locationToUpdateJson = jacksonObjectMapper.writeValueAsString(locationToUpdate);

        String gotLocationJson = mockMvc.perform(put("/locations/{id}", createdLocation.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(locationToUpdateJson))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Location gotLocation = jacksonObjectMapper.readValue(gotLocationJson, Location.class);

        locationToUpdate.setId(createdLocation.getId());

        org.assertj.core.api.Assertions.assertThat(locationConverter.toDomain(locationToUpdate)).usingRecursiveComparison().isEqualTo(gotLocation);
    }

    @Test
    public void shouldSuccessOnDeleteLocation() throws Exception {
        Location sourceLocation = new Location(null,
                "Hermitage",
                "Saint-Petersburg City, Dvortsovaya ploschad, 1",
                1000,
                null);
        Location createdLocation = locationService.createLocation(sourceLocation);

        String deletedLocationJson = mockMvc.perform(delete("/locations/{id}", createdLocation.getId()))
                .andExpect(status().is(HttpStatus.OK.value()))
                        .andReturn()
                                .getResponse().getContentAsString();
        Location deletedLocation = jacksonObjectMapper.readValue(deletedLocationJson, Location.class);
        org.assertj.core.api.Assertions.assertThat(deletedLocation).usingRecursiveComparison().isEqualTo(createdLocation);

        mockMvc.perform(get("/locations/{id}", createdLocation.getId()))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }
}
