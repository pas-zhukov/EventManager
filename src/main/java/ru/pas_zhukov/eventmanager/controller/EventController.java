package ru.pas_zhukov.eventmanager.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.pas_zhukov.eventmanager.converter.EventConverter;
import ru.pas_zhukov.eventmanager.dto.request.EventCreateRequestDto;
import ru.pas_zhukov.eventmanager.dto.request.EventSearchRequestDto;
import ru.pas_zhukov.eventmanager.dto.request.EventUpdateRequestDto;
import ru.pas_zhukov.eventmanager.dto.response.EventResponseDto;
import ru.pas_zhukov.eventmanager.entity.UserEntity;
import ru.pas_zhukov.eventmanager.model.Event;
import ru.pas_zhukov.eventmanager.model.User;
import ru.pas_zhukov.eventmanager.model.UserRole;
import ru.pas_zhukov.eventmanager.security.AuthenticationService;
import ru.pas_zhukov.eventmanager.service.EventPermissionService;
import ru.pas_zhukov.eventmanager.service.EventService;
import ru.pas_zhukov.eventmanager.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventConverter eventConverter;
    private final EventService eventService;
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final EventPermissionService eventPermissionService;

    public EventController(EventConverter eventConverter, EventService eventService, UserService userService, AuthenticationService authenticationService, EventPermissionService eventPermissionService) {
        this.eventConverter = eventConverter;
        this.eventService = eventService;
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.eventPermissionService = eventPermissionService;
    }

    @PostMapping
    public ResponseEntity<EventResponseDto> createEvent(@Valid @RequestBody EventCreateRequestDto eventCreateRequestDto) {
        User authenticatedUser = authenticationService.getCurrentAuthenticatedUserOrThrow();
        Event createdEvent = eventService.createEvent(authenticatedUser, eventConverter.toDomain(eventCreateRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(eventConverter.toResponseDto(createdEvent));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> getEvent(@PathVariable("eventId") Long eventId) {
        Event foundEvent =  eventService.getEventByIdOrThrow(eventId);
        return ResponseEntity.ok(eventConverter.toResponseDto(foundEvent));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<HttpStatus> deleteEvent(@PathVariable("eventId") Long eventId) {
        eventPermissionService.throwOnCurrentlyAuthenticatedUserCantModifyEvent(eventId);
        eventService.deleteEventById(eventId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> updateEvent(@PathVariable("eventId") Long eventId, @Valid @RequestBody EventUpdateRequestDto eventUpdateRequestDto) {
        eventPermissionService.throwOnCurrentlyAuthenticatedUserCantModifyEvent(eventId);
        Event updatedEvent = eventService.updateEvent(eventId, eventConverter.toDomain(eventUpdateRequestDto));
        return ResponseEntity.ok(eventConverter.toResponseDto(updatedEvent));
    }

    @PostMapping("/search")
    public ResponseEntity<List<EventResponseDto>> searchEvents(@Valid @RequestBody EventSearchRequestDto eventSearchRequestDto) {
        List<Event> foundEvents = eventService.searchEvents(eventSearchRequestDto);
        return ResponseEntity.ok(foundEvents.stream().map(eventConverter::toResponseDto).toList());
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventResponseDto>> getUserEvents() {
        User user = authenticationService.getCurrentAuthenticatedUserOrThrow();
        List<Event> usersEvents = eventService.getUserOwnedEvents(user);
        return ResponseEntity.ok(usersEvents.stream().map(eventConverter::toResponseDto).toList());
    }

}
