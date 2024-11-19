package ru.pas_zhukov.eventmanager.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pas_zhukov.eventmanager.converter.EventConverter;
import ru.pas_zhukov.eventmanager.dto.request.EventCreateRequestDto;
import ru.pas_zhukov.eventmanager.dto.request.EventSearchRequestDto;
import ru.pas_zhukov.eventmanager.dto.request.EventUpdateRequestDto;
import ru.pas_zhukov.eventmanager.dto.response.EventResponseDto;
import ru.pas_zhukov.eventmanager.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventConverter eventConverter;
    private final EventService eventService;

    public EventController(EventConverter eventConverter, EventService eventService) {
        this.eventConverter = eventConverter;
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventResponseDto> createEvent(@Valid @RequestBody EventCreateRequestDto eventCreateRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventConverter.toResponseDto(eventService.createEvent(eventConverter.toDomain(eventCreateRequestDto))));
    }

    @GetMapping
    @RequestMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> getEvent(@PathVariable("eventId") Long eventId) {
        return null;
    }

    @DeleteMapping
    @RequestMapping("/{eventId}")
    public ResponseEntity<HttpStatus> deleteEvent(@PathVariable("eventId") Long eventId) {
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @RequestMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> updateEvent(@PathVariable("eventId") Long eventId, @Valid @RequestBody EventUpdateRequestDto eventUpdateRequestDto) {
        return null;
    }

    @PostMapping
    @RequestMapping("/search")
    public ResponseEntity<List<EventResponseDto>> searchEvents(@Valid @RequestBody EventSearchRequestDto eventSearchRequestDto) {
        return null;
    }

    @GetMapping
    @RequestMapping("/my")
    public ResponseEntity<List<EventResponseDto>> getUserEvents() {
        return null;
    }

}
