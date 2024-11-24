package ru.pas_zhukov.eventmanager.service;

import org.springframework.stereotype.Service;
import ru.pas_zhukov.eventmanager.converter.EventConverter;
import ru.pas_zhukov.eventmanager.converter.UserConverter;
import ru.pas_zhukov.eventmanager.entity.EventEntity;
import ru.pas_zhukov.eventmanager.entity.RegistrationEntity;
import ru.pas_zhukov.eventmanager.model.Event;
import ru.pas_zhukov.eventmanager.model.Registration;
import ru.pas_zhukov.eventmanager.model.User;
import ru.pas_zhukov.eventmanager.repository.EventRepository;
import ru.pas_zhukov.eventmanager.repository.RegistrationRepository;

import java.util.List;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final UserConverter userConverter;
    private final EventConverter eventConverter;
    private final EventService eventService;
    private final EventRepository eventRepository;

    public RegistrationService(RegistrationRepository registrationRepository, UserConverter userConverter, EventConverter eventConverter, EventService eventService, EventRepository eventRepository) {
        this.registrationRepository = registrationRepository;
        this.userConverter = userConverter;
        this.eventConverter = eventConverter;
        this.eventService = eventService;
        this.eventRepository = eventRepository;
    }

    public Registration registerUserOnEvent(User user, Event event) {
        if (registrationRepository.existsByUserAndEvent(userConverter.toEntity(user), eventConverter.toEntity(event))) {
            throw new IllegalStateException("User with id=%s already registered on event with id=%s".formatted(user.getId(), event.getId()));
        }
        RegistrationEntity newRegistration = new RegistrationEntity(null, userConverter.toEntity(user), eventConverter.toEntity(event));
        newRegistration = registrationRepository.save(newRegistration);
        return new Registration(newRegistration.getId(), userConverter.toDomain(newRegistration.getUser()), eventConverter.toDomain(newRegistration.getEvent()));
    }

    public void cancelUserRegistrationOnEvent(User user, Event event) {
        RegistrationEntity foundRegistration = registrationRepository.findByUserAndEvent(userConverter.toEntity(user), eventConverter.toEntity(event)).orElseThrow(
                () -> new IllegalStateException("Registration for user with id=%s on event with id=%s not found".formatted(user.getId(), event.getId()))
        );
        registrationRepository.delete(foundRegistration);
    }

    public List<Event> getUserRegisteredEvents(User user) {
        List<RegistrationEntity> userRegistrations = registrationRepository.findAllByUserIs(userConverter.toEntity(user));
        List<EventEntity> userRegisteredEvents = eventRepository.findAllByIdIsIn(userRegistrations.stream().mapToLong(e -> e.getEvent().getId()).boxed().toList());
        return userRegisteredEvents.stream().map(eventConverter::toDomain).toList();
    }
}