package ru.pas_zhukov.eventmanager.service;


import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.pas_zhukov.eventmanager.converter.EventConverter;
import ru.pas_zhukov.eventmanager.converter.UserConverter;
import ru.pas_zhukov.eventmanager.entity.EventEntity;
import ru.pas_zhukov.eventmanager.entity.UserEntity;
import ru.pas_zhukov.eventmanager.model.Event;
import ru.pas_zhukov.eventmanager.model.EventStatus;
import ru.pas_zhukov.eventmanager.model.User;
import ru.pas_zhukov.eventmanager.repository.EventRepository;
import ru.pas_zhukov.eventmanager.repository.UserRepository;
import ru.pas_zhukov.eventmanager.security.AuthenticationService;

@Service
public class EventService {

    private final AuthenticationService authenticationService;
    private final EventConverter eventConverter;
    private final UserConverter userConverter;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventService(AuthenticationService authenticationService, EventConverter eventConverter, UserConverter userConverter, EventRepository eventRepository, UserRepository userRepository) {
        this.authenticationService = authenticationService;
        this.eventConverter = eventConverter;
        this.userConverter = userConverter;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
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

}
