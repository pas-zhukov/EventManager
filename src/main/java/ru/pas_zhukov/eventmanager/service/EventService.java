package ru.pas_zhukov.eventmanager.service;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.pas_zhukov.eventmanager.converter.EventConverter;
import ru.pas_zhukov.eventmanager.converter.UserConverter;
import ru.pas_zhukov.eventmanager.dto.request.EventSearchRequestDto;
import ru.pas_zhukov.eventmanager.entity.EventEntity;
import ru.pas_zhukov.eventmanager.entity.UserEntity;
import ru.pas_zhukov.eventmanager.model.Event;
import ru.pas_zhukov.eventmanager.model.EventStatus;
import ru.pas_zhukov.eventmanager.model.User;
import ru.pas_zhukov.eventmanager.model.UserRole;
import ru.pas_zhukov.eventmanager.repository.EventRepository;
import ru.pas_zhukov.eventmanager.repository.UserRepository;
import ru.pas_zhukov.eventmanager.security.AuthenticationService;

import java.util.List;

@Service
public class EventService {

    private final AuthenticationService authenticationService;
    private final EventConverter eventConverter;
    private final UserConverter userConverter;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public EventService(AuthenticationService authenticationService, EventConverter eventConverter, UserConverter userConverter, EventRepository eventRepository, UserRepository userRepository, UserService userService) {
        this.authenticationService = authenticationService;
        this.eventConverter = eventConverter;
        this.userConverter = userConverter;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public Event createEvent(Event eventToCreate) {
        UserEntity user = userRepository.findByLogin(authenticationService.getCurrentAuthenticatedUserOrThrow().getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
        eventToCreate.setOwner(userConverter.toDomain(user));
        eventToCreate.setStatus(EventStatus.WAIT_START);
        EventEntity eventEntityToCreate = eventConverter.toEntity(eventToCreate);
        EventEntity createdEvent = eventRepository.save(eventEntityToCreate);
        return eventConverter.toDomain(createdEvent);
    }

    public Event updateEvent(Long id, Event eventToUpdate) {
        User user = userService.getUserByLogin(authenticationService.getCurrentAuthenticatedUserOrThrow().getUsername());
        Event event = getEventById(id);
        if (user.getRole() != UserRole.ADMIN || !event.getOwner().equals(user)) {
            throw new AccessDeniedException("Only admins or owners can update events");
        }
        event = event.mergeEvent(eventToUpdate);
        eventRepository.save(eventConverter.toEntity(event));
        return event;
    }


    public Event getEventById(Long id) {
        EventEntity eventEntity = eventRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Event with id=%s not found".formatted(id))
        );
        return eventConverter.toDomain(eventEntity);
    }

    public Event deleteEventById(Long id) {
        User user = userService.getUserByLogin(authenticationService.getCurrentAuthenticatedUserOrThrow().getUsername());
        Event eventToDelete = getEventById(id);
        if (user.getRole() != UserRole.ADMIN || !eventToDelete.getOwner().equals(user)) {
            throw new AccessDeniedException("Only admins or owners can delete events");
        }
        eventRepository.removeEventEntityById(id);
        return eventToDelete;
    }

    public List<Event> getCurrentUserOwnedEvents() {
        User user = userService.getUserByLogin(authenticationService.getCurrentAuthenticatedUserOrThrow().getUsername());
        List<EventEntity> userOwnedEvents = eventRepository.findAllByOwnerIs(userConverter.toEntity(user));
        return userOwnedEvents.stream().map(eventConverter::toDomain).toList();
    }

    public List<Event> searchEvents(EventSearchRequestDto searchRequestDto) {
        List<EventEntity> foundEvents = eventRepository.searchEvents(
                searchRequestDto.getName(),
                searchRequestDto.getPlacesMin(),
                searchRequestDto.getPlacesMax(),
                searchRequestDto.getDateStartAfter(),
                searchRequestDto.getDateStartBefore(),
                searchRequestDto.getCostMin(),
                searchRequestDto.getCostMax(),
                searchRequestDto.getDurationMin(),
                searchRequestDto.getDurationMax(),
                searchRequestDto.getLocationId(),
                searchRequestDto.getEventStatus()
        );
        return foundEvents.stream().map(eventConverter::toDomain).toList();
    }



}
