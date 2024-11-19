package ru.pas_zhukov.eventmanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pas_zhukov.eventmanager.dto.response.EventResponseDto;

import java.util.List;

@RestController
@RequestMapping("events/registrations")
public class EventRegistrationController {

    @PostMapping("/{eventId}")
    public ResponseEntity<HttpStatus> registerUserOnEvent(@PathVariable("eventId") Long eventId) {
        return null;
    }

    @DeleteMapping("/cancel/{eventId}")
    public ResponseEntity<HttpStatus> cancelUserRegistrationOnEvent(@PathVariable("eventId") Long eventId) {
        return null;
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventResponseDto>> getUserRegistrationsEvents() {
        return null;
    }
}
