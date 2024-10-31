package ru.pas_zhukov.eventmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import ru.pas_zhukov.eventmanager.converter.LocationConverter;
import ru.pas_zhukov.eventmanager.dto.request.LocationRequestDto;
import ru.pas_zhukov.eventmanager.model.Location;
import ru.pas_zhukov.eventmanager.service.LocationService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class LocationControllerTest {

    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationConverter locationConverter;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper jacksonObjectMapper;

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

        Assertions.assertThat(createdLocation).usingRecursiveComparison().isEqualTo(gotLocation);
    }

    @Test
    public void shouldReturnNotFoundOnGetLocationByNotExistingId() throws Exception {
        mockMvc.perform(get("/locations/{id}", Long.MAX_VALUE))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }
}
