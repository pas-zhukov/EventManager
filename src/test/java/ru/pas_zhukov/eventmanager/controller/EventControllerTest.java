package ru.pas_zhukov.eventmanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import ru.pas_zhukov.eventmanager.TestInContainer;
import ru.pas_zhukov.eventmanager.dto.request.EventCreateRequestDto;
import ru.pas_zhukov.eventmanager.dto.response.EventResponseDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EventControllerTest extends TestInContainer {

    @Test
    @WithMockUser(username = "testuser", authorities = {"USER"})
    public void shouldSuccessCreateEvent() throws Exception {
        String eventToCreateJson = """
                        {
                          "date": "2000-01-23T04:56:07.000+00:00",
                          "duration": 60,
                          "cost": 1200,
                          "maxPlaces": 10,
                          "locationId": 10,
                          "name": "Лекция по Java"
                        }""";
        EventCreateRequestDto eventToCreate = jacksonObjectMapper.readValue(
                eventToCreateJson,
                EventCreateRequestDto.class
        );

        String responseJson = mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(eventToCreateJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        EventResponseDto responseDto = jacksonObjectMapper.readValue(responseJson, EventResponseDto.class);
        System.out.println(responseJson);
    }

    public void shouldNotCreateEventOnInvalidRequest() {

    }

    public void shouldSuccessDeleteEvent() {

    }

    public void shouldNotDeleteEventWhenRequestIsNotFromOwnerOrAdmin() {

    }

    public void shouldSuccessOnGetEventById() {

    }

    public void shouldReturnNotFoundOnGetNonExistingEventById() {

    }

    public void shouldSuccessOnUpdateEvent() {

    }

    public void shouldNotUpdateEventOnInvalidRequest() {

    }


}
