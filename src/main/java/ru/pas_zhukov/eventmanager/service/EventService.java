package ru.pas_zhukov.eventmanager.service;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ru.pas_zhukov.eventmanager.converter.EventConverter;
import ru.pas_zhukov.eventmanager.converter.UserConverter;
import ru.pas_zhukov.eventmanager.dto.request.EventSearchRequestDto;
import ru.pas_zhukov.eventmanager.entity.EventEntity;
import ru.pas_zhukov.eventmanager.entity.RegistrationEntity;
import ru.pas_zhukov.eventmanager.entity.UserEntity;
import ru.pas_zhukov.eventmanager.kafka.EventChangeMessage;
import ru.pas_zhukov.eventmanager.kafka.EventSender;
import ru.pas_zhukov.eventmanager.kafka.FieldChange;
import ru.pas_zhukov.eventmanager.kafka.MessageType;
import ru.pas_zhukov.eventmanager.model.*;
import ru.pas_zhukov.eventmanager.repository.EventRepository;
import ru.pas_zhukov.eventmanager.repository.RegistrationRepository;
import ru.pas_zhukov.eventmanager.security.AuthenticationService;

import java.util.List;

@Service
public class EventService {

    private final EventConverter eventConverter;
    private final UserConverter userConverter;
    private final EventRepository eventRepository;
    private final LocationService locationService;
    private final EventSender eventSender;
    private final RegistrationRepository registrationRepository;
    private final AuthenticationService authenticationService;

    public EventService(EventConverter eventConverter, UserConverter userConverter, EventRepository eventRepository, LocationService locationService, EventSender eventSender, RegistrationRepository registrationRepository, AuthenticationService authenticationService) {
        this.eventConverter = eventConverter;
        this.userConverter = userConverter;
        this.eventRepository = eventRepository;
        this.locationService = locationService;
        this.eventSender = eventSender;
        this.registrationRepository = registrationRepository;
        this.authenticationService = authenticationService;
    }

    public Event createEvent(User owner, Event eventToCreate) {
        validateEventLocationPlacesQuantity(eventToCreate);
        eventToCreate.setOwner(owner);
        eventToCreate.setStatus(EventStatus.WAIT_START);
        eventToCreate.setOccupiedPlaces(0);
        EventEntity eventEntityToCreate = eventConverter.toEntity(eventToCreate);
        EventEntity createdEvent = eventRepository.save(eventEntityToCreate);
        eventSender.sendEvent(EventChangeMessage.builder()
                .withMessageType(MessageType.CREATED)
                .withEventId(createdEvent.getId())
                .withOwner(owner)
                .withChangedByUserId(owner.getId())
                .withUserLogins(createdEvent.getRegistrations().stream().map(RegistrationEntity::getUser).map(UserEntity::getLogin).toList())
                .withCostChange(null, createdEvent.getCost())
                .withDateChange(null, createdEvent.getDate())
                .withNameChange(null, createdEvent.getName())
                .withDurationChange(null, createdEvent.getDuration())
                .withLocationIdChange(null, createdEvent.getLocation().getId())
                .withMaxPlacesChange(null, createdEvent.getMaxPlaces())
                .withStatusChange(null, createdEvent.getStatus())
                .build());
        return eventConverter.toDomain(createdEvent);
    }

    public Event updateEvent(Long id, Event eventToUpdate) {
        Event event = getEventByIdOrThrow(id);
        validateEventLocationPlacesQuantity(eventToUpdate);
        if (eventToUpdate.getMaxPlaces() < event.getOccupiedPlaces()) {
            throw new IllegalStateException("MaxPlaces must be equal or greater than occupiedPlaces");
        }
        EventChangeMessage message = generateUpdateEventKafkaMessage(event, eventToUpdate);
        event = event.mergeEvent(eventToUpdate);
        eventRepository.save(eventConverter.toEntity(event));
        eventSender.sendEvent(message);
        return event;
    }

    private EventChangeMessage generateUpdateEventKafkaMessage(Event event, Event eventToUpdate) {
        User authenticatedUser = authenticationService.getCurrentAuthenticatedUserOrThrow();

        List<String> eventUsersLogins = eventRepository.findById(event.getId()).orElseThrow(
                () -> new EntityNotFoundException("Event with id=%s not found".formatted(event.getId()))
        )
                .getRegistrations().stream().map(RegistrationEntity::getUser).map(UserEntity::getLogin).toList();

        EventChangeMessage message = EventChangeMessage.builder()
                .withMessageType(MessageType.UPDATED)
                .withEventId(event.getId())
                .withChangedByUserId(authenticatedUser.getId())
                .withOwner(event.getOwner())
                .withUserLogins(eventUsersLogins)
                .build();

        message.setName(eventToUpdate.getName() != null ? new FieldChange<>(event.getName(), eventToUpdate.getName()) : null);
        message.setMaxPlaces(eventToUpdate.getMaxPlaces() != null ? new FieldChange<>(event.getMaxPlaces(), eventToUpdate.getMaxPlaces()) : null);
        message.setDate(eventToUpdate.getDate() != null ? new FieldChange<>(event.getDate(), eventToUpdate.getDate()) : null);
        message.setCost(eventToUpdate.getCost() != null ? new FieldChange<>(event.getCost(), eventToUpdate.getCost()) : null);
        message.setDuration(eventToUpdate.getDuration() != null ? new FieldChange<>(event.getDuration(), event.getDuration()) : null);
        message.setLocationId(eventToUpdate.getLocation() != null ? new FieldChange<>(event.getLocation().getId(), eventToUpdate.getLocation().getId()) : null);
        message.setStatus(eventToUpdate.getStatus() != null ? new FieldChange<>(event.getStatus(), eventToUpdate.getStatus()) : null);
        return message;
    }

    public void validateEventLocationPlacesQuantity(Event event) {
        Location location = locationService.getLocationById(event.getLocation().getId());
        if (location.getCapacity() < event.getMaxPlaces()) {
            throw new IllegalStateException("Location capacity must be equal or less than event maxPlaces");
        }
    }

    public Event getEventByIdOrThrow(Long id) {
        EventEntity eventEntity = eventRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Event with id=%s not found".formatted(id))
        );
        return eventConverter.toDomain(eventEntity);
    }

    public void deleteEventById(Long id) {
        User authenticatedUser = authenticationService.getCurrentAuthenticatedUserOrThrow();
        Event eventToDelete = getEventByIdOrThrow(id);
        List<RegistrationEntity> registrations = registrationRepository.findAllByEventId(id);
        eventSender.sendEvent(
                EventChangeMessage.builder()
                        .withMessageType(MessageType.DELETED)
                        .withOwnerId(eventToDelete.getOwner().getId())
                        .withChangedByUserId(authenticatedUser.getId())
                        .withUserLogins(registrations.stream().map(RegistrationEntity::getUser).map(UserEntity::getLogin).toList())
                        .withEventId(id)
                        .build()
        );
        eventRepository.removeEventEntityById(id);
    }

    public List<Event> getUserOwnedEvents(User user) {
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


    public void increaseOccupiedPlacesOrThrow(Event event) {
        if (event.getOccupiedPlaces() >= event.getMaxPlaces()) {
            throw new IllegalStateException("No more free places");
        }
        verifyEventNotFinishedAndNotCancelledOrThrow(event);
        eventRepository.increaseOccupiedPlacesByEventId(event.getId());
    }

    public void decreaseOccupiedPlacesOrThrow(Event event) {
        verifyEventNotFinishedAndNotCancelledOrThrow(event);
        eventRepository.decreaseOccupiedPlacesByEventId(event.getId());
    }

    public void verifyEventNotFinishedAndNotCancelledOrThrow(Event event) {
        if (event.getStatus().equals(EventStatus.FINISHED)) {
            throw new IllegalStateException("Can't modify event that is finished");
        } else if (event.getStatus().equals(EventStatus.CANCELLED)) {
            throw new IllegalStateException("Can't modify event that is cancelled");
        }
    }
}
