package ru.pas_zhukov.eventmanager.service;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ru.pas_zhukov.eventmanager.converter.EventConverter;
import ru.pas_zhukov.eventmanager.converter.UserConverter;
import ru.pas_zhukov.eventmanager.dto.request.EventSearchRequestDto;
import ru.pas_zhukov.eventmanager.entity.EventEntity;
import ru.pas_zhukov.eventmanager.model.Event;
import ru.pas_zhukov.eventmanager.model.EventStatus;
import ru.pas_zhukov.eventmanager.model.Location;
import ru.pas_zhukov.eventmanager.model.User;
import ru.pas_zhukov.eventmanager.repository.EventRepository;

import java.util.List;

@Service
public class EventService {

    private final EventConverter eventConverter;
    private final UserConverter userConverter;
    private final EventRepository eventRepository;
    private final LocationService locationService;

    public EventService(EventConverter eventConverter, UserConverter userConverter, EventRepository eventRepository, LocationService locationService) {
        this.eventConverter = eventConverter;
        this.userConverter = userConverter;
        this.eventRepository = eventRepository;
        this.locationService = locationService;
    }

    public Event createEvent(User owner, Event eventToCreate) {
        validateEventLocationPlacesQuantity(eventToCreate);
        eventToCreate.setOwner(owner);
        eventToCreate.setStatus(EventStatus.WAIT_START);
        eventToCreate.setOccupiedPlaces(0);
        EventEntity eventEntityToCreate = eventConverter.toEntity(eventToCreate);
        EventEntity createdEvent = eventRepository.save(eventEntityToCreate);
        return eventConverter.toDomain(createdEvent);
    }

    public Event updateEvent(Long id, Event eventToUpdate) {
        Event event = getEventByIdOrThrow(id);
        validateEventLocationPlacesQuantity(eventToUpdate);
        if (eventToUpdate.getMaxPlaces() < event.getOccupiedPlaces()) {
            throw new IllegalStateException("MaxPlaces must be equal or greater than occupiedPlaces");
        }
        event = event.mergeEvent(eventToUpdate);
        eventRepository.save(eventConverter.toEntity(event));
        return event;
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
        Event eventToDelete = getEventByIdOrThrow(id);
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
