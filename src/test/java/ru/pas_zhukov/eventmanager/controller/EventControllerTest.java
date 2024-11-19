package ru.pas_zhukov.eventmanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import ru.pas_zhukov.eventmanager.TestInContainer;
import ru.pas_zhukov.eventmanager.converter.LocationConverter;
import ru.pas_zhukov.eventmanager.dto.request.EventCreateRequestDto;
import ru.pas_zhukov.eventmanager.dto.request.LocationRequestDto;
import ru.pas_zhukov.eventmanager.dto.response.EventResponseDto;
import ru.pas_zhukov.eventmanager.model.Location;
import ru.pas_zhukov.eventmanager.service.LocationService;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EventControllerTest extends TestInContainer {

    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationConverter locationConverter;

    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    public void shouldSuccessCreateEvent() throws Exception {

        LocationRequestDto locationRequestDto = new LocationRequestDto(null,
                "Hermitage",
                "Saint-Petersburg City, Dvortsovaya ploschad, 1",
                1000,
                null);
        Location createdLocation = locationService.createLocation(locationConverter.toDomain(locationRequestDto));

        EventCreateRequestDto eventToCreate = new EventCreateRequestDto(
                "Lekcii Java",
                10,
                new Date(),
                BigDecimal.valueOf(1200),
                60,
                createdLocation.getId()
        );

        String eventToCreateJson = jacksonObjectMapper.writeValueAsString(eventToCreate);

        String responseJson = mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(eventToCreateJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        EventResponseDto createdEventDto = jacksonObjectMapper.readValue(responseJson, EventResponseDto.class);

        Assertions.assertNotNull(createdEventDto.getId());
        Assertions.assertEquals(eventToCreate.getName(), createdEventDto.getName());
        Assertions.assertEquals(eventToCreate.getMaxPlaces(), createdEventDto.getMaxPlaces());
        Assertions.assertEquals(eventToCreate.getDate(), createdEventDto.getDate());
        Assertions.assertEquals(eventToCreate.getCost(), createdEventDto.getCost());
        Assertions.assertEquals(eventToCreate.getDuration(), createdEventDto.getDuration());
        Assertions.assertEquals(eventToCreate.getLocationId(), createdEventDto.getLocationId());
    }

    @Test
    public void shouldNotCreateEventOnInvalidRequest() {
        throw new RuntimeException();
    }

    @Test
    public void shouldSuccessDeleteEvent() {
        throw new RuntimeException();
    }

    @Test
    public void shouldNotDeleteEventWhenRequestIsNotFromOwnerOrAdmin() {
        throw new RuntimeException();
    }

    @Test
    public void shouldSuccessOnGetEventById() {
        throw new RuntimeException();
    }

    @Test
    public void shouldReturnNotFoundOnGetNonExistingEventById() {
        throw new RuntimeException();
    }

    @Test
    public void shouldSuccessOnUpdateEvent() {
        throw new RuntimeException();
    }

    @Test
    public void shouldNotUpdateEventOnInvalidRequest() {
        throw new RuntimeException();
    }


}
