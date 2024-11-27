package ru.pas_zhukov.eventmanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pas_zhukov.eventmanager.converter.EventConverter;
import ru.pas_zhukov.eventmanager.dto.response.EventResponseDto;
import ru.pas_zhukov.eventmanager.model.Event;
import ru.pas_zhukov.eventmanager.model.User;
import ru.pas_zhukov.eventmanager.security.AuthenticationService;
import ru.pas_zhukov.eventmanager.service.EventService;
import ru.pas_zhukov.eventmanager.service.RegistrationService;

import java.util.List;

@RestController
@RequestMapping("events/registrations")
public class EventRegistrationController {

    private final AuthenticationService authenticationService;
    private final EventService eventService;
    private final RegistrationService registrationService;
    private final EventConverter eventConverter;

    public EventRegistrationController(AuthenticationService authenticationService, EventService eventService, RegistrationService registrationService, EventConverter eventConverter) {
        this.authenticationService = authenticationService;
        this.eventService = eventService;
        this.registrationService = registrationService;
        this.eventConverter = eventConverter;
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<HttpStatus> registerUserOnEvent(@PathVariable("eventId") Long eventId) {
        User user = authenticationService.getCurrentAuthenticatedUserOrThrow();
        Event event = eventService.getEventByIdOrThrow(eventId);
        registrationService.registerUserOnEvent(user, event);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cancel/{eventId}")
    public ResponseEntity<HttpStatus> cancelUserRegistrationOnEvent(@PathVariable("eventId") Long eventId) {
        User user = authenticationService.getCurrentAuthenticatedUserOrThrow();
        Event event = eventService.getEventByIdOrThrow(eventId);
        registrationService.cancelUserRegistrationOnEvent(user, event);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventResponseDto>> getUserRegistrationsEvents() {
        User user = authenticationService.getCurrentAuthenticatedUserOrThrow();
        List<Event> userRegisteredEvents = registrationService.getUserRegisteredEvents(user);
        return ResponseEntity.status(HttpStatus.OK).body(userRegisteredEvents.stream().map(eventConverter::toResponseDto).toList());
    }
}
