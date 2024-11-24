package ru.pas_zhukov.eventmanager.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import ru.pas_zhukov.eventmanager.TestInContainer;
import ru.pas_zhukov.eventmanager.converter.EventConverter;
import ru.pas_zhukov.eventmanager.converter.LocationConverter;
import ru.pas_zhukov.eventmanager.converter.UserConverter;
import ru.pas_zhukov.eventmanager.dto.request.EventCreateRequestDto;
import ru.pas_zhukov.eventmanager.dto.request.LocationRequestDto;
import ru.pas_zhukov.eventmanager.entity.UserEntity;
import ru.pas_zhukov.eventmanager.model.Event;
import ru.pas_zhukov.eventmanager.model.Location;
import ru.pas_zhukov.eventmanager.model.User;
import ru.pas_zhukov.eventmanager.repository.UserRepository;
import ru.pas_zhukov.eventmanager.security.AuthenticationService;

import java.math.BigDecimal;
import java.util.Date;

public class EventServiceTest extends TestInContainer {

    @Autowired
    private EventService eventService;
    @Autowired
    private EventConverter eventConverter;
    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationConverter locationConverter;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserConverter userConverter;

    @Test
    @Transactional
    @WithMockUser(username = "user", authorities = {"USER"})
    public void shouldSuccessCreateEvent() {

        LocationRequestDto locationRequestDto = new LocationRequestDto(null,
                "Hermitage",
                "Saint-Petersburg City, Dvortsovaya ploschad, 1",
                1000,
                null);
        Location createdLocation = locationService.createLocation(locationConverter.toDomain(locationRequestDto));

        EventCreateRequestDto eventToCreateDto = new EventCreateRequestDto(
                "Lekcii Java",
                10,
                new Date(),
                BigDecimal.valueOf(1200),
                60,
                createdLocation.getId()
        );

        User user = authenticationService.getCurrentAuthenticatedUserOrThrow();

        Event eventToCreate = eventConverter.toDomain(eventToCreateDto);
        Event createdEvent = eventService.createEvent(user, eventToCreate);

        Assertions.assertNotNull(createdEvent.getId());
        Assertions.assertEquals(eventToCreate.getName(), createdEvent.getName());
        Assertions.assertEquals(eventToCreate.getMaxPlaces(), createdEvent.getMaxPlaces());
        Assertions.assertEquals(eventToCreate.getDate(), createdEvent.getDate());
        Assertions.assertEquals(eventToCreate.getCost(), createdEvent.getCost());
        Assertions.assertEquals(eventToCreate.getDuration(), createdEvent.getDuration());
        Assertions.assertEquals(eventToCreate.getLocation().getId(), createdEvent.getLocation().getId());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void shouldSuccessUpdateEvent() throws Exception {

    }
}
