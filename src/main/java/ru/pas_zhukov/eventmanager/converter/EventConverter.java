package ru.pas_zhukov.eventmanager.converter;

import org.springframework.stereotype.Component;
import ru.pas_zhukov.eventmanager.dto.request.EventCreateRequestDto;
import ru.pas_zhukov.eventmanager.dto.response.EventResponseDto;
import ru.pas_zhukov.eventmanager.entity.EventEntity;
import ru.pas_zhukov.eventmanager.model.Event;
import ru.pas_zhukov.eventmanager.model.Location;

@Component
public class EventConverter {
    private final LocationConverter locationConverter;
    private final UserConverter userConverter;

    public EventConverter(LocationConverter locationConverter, UserConverter userConverter) {
        this.locationConverter = locationConverter;
        this.userConverter = userConverter;
    }

    public EventEntity toEntity(Event event) {
        return new EventEntity(event.getId(),
                event.getName(),
                userConverter.toEntity(event.getOwner()),
                event.getMaxPlaces(),
                event.getOccupiedPlaces(),
                event.getDate(),
                event.getCost(),
                event.getDuration(),
                locationConverter.toEntity(event.getLocation()),
                event.getStatus());
    }

    public Event toDomain(EventEntity eventEntity) {
        return new Event(eventEntity.getId(),
                eventEntity.getName(),
                userConverter.toDomain(eventEntity.getOwner()),
                eventEntity.getMaxPlaces(),
                eventEntity.getOccupiedPlaces(),
                eventEntity.getDate(),
                eventEntity.getCost(),
                eventEntity.getDuration(),
                locationConverter.toDomain(eventEntity.getLocation()),
                eventEntity.getStatus());
    }

    public EventResponseDto toResponseDto(Event event) {
        return new EventResponseDto(event.getId(),
                event.getName(),
                event.getOwner().getId(),
                event.getMaxPlaces(),
                event.getOccupiedPlaces(),
                event.getDate(),
                event.getCost(),
                event.getDuration(),
                event.getLocation().getId(),
                event.getStatus());
    }

    public Event toDomain(EventCreateRequestDto eventCreateRequestDto) {
        Event event = new Event();
        event.setName(eventCreateRequestDto.getName());
        event.setMaxPlaces(eventCreateRequestDto.getMaxPlaces());
        event.setDate(eventCreateRequestDto.getDate());
        event.setCost(eventCreateRequestDto.getCost());
        event.setDuration(eventCreateRequestDto.getDuration());
        Location location = new Location();
        location.setId(eventCreateRequestDto.getLocationId());
        event.setLocation(location);
        return event;
    }
}
