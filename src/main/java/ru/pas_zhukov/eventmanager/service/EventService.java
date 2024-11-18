package ru.pas_zhukov.eventmanager.service;


import org.springframework.stereotype.Service;
import ru.pas_zhukov.eventmanager.converter.EventConverter;
import ru.pas_zhukov.eventmanager.converter.UserConverter;
import ru.pas_zhukov.eventmanager.entity.EventEntity;
import ru.pas_zhukov.eventmanager.model.Event;
import ru.pas_zhukov.eventmanager.model.EventStatus;
import ru.pas_zhukov.eventmanager.model.User;
import ru.pas_zhukov.eventmanager.repository.EventRepository;
import ru.pas_zhukov.eventmanager.security.AuthenticationService;

@Service
public class EventService {

    private final AuthenticationService authenticationService;
    private final EventConverter eventConverter;
    private final UserConverter userConverter;
    private final EventRepository eventRepository;

    public EventService(AuthenticationService authenticationService, EventConverter eventConverter, UserConverter userConverter, EventRepository eventRepository) {
        this.authenticationService = authenticationService;
        this.eventConverter = eventConverter;
        this.userConverter = userConverter;
        this.eventRepository = eventRepository;
    }

    public Event createEvent(Event eventToCreate) {
        User user = authenticationService.getCurrentAuthenticatedUserOrThrow();
        EventEntity eventEntityToCreate = eventConverter.toEntity(eventToCreate);
        eventEntityToCreate.setOwner(userConverter.toEntity(user));
        eventEntityToCreate.setStatus(EventStatus.WAIT_START);
        EventEntity createdEvent = eventRepository.save(eventEntityToCreate);
        return eventConverter.toDomain(createdEvent);
    }

}
