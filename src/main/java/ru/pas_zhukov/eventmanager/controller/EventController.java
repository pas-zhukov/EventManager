package ru.pas_zhukov.eventmanager.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pas_zhukov.eventmanager.converter.EventConverter;
import ru.pas_zhukov.eventmanager.dto.request.EventCreateRequestDto;
import ru.pas_zhukov.eventmanager.dto.response.EventResponseDto;
import ru.pas_zhukov.eventmanager.service.EventService;

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

}
