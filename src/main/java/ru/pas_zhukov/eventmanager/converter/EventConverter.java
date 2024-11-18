package ru.pas_zhukov.eventmanager.converter;

import org.springframework.stereotype.Component;
import ru.pas_zhukov.eventmanager.dto.request.EventCreateRequestDto;
import ru.pas_zhukov.eventmanager.dto.response.EventResponseDto;
import ru.pas_zhukov.eventmanager.entity.EventEntity;
import ru.pas_zhukov.eventmanager.model.Event;

@Component
public class EventConverter {
    public EventEntity toEntity(Event event) {
        return null;
    }

    public Event toDomain(EventEntity eventEntity) {
        return null;
    }

    public EventResponseDto toResponseDto(Event event) {
        return null;
    }

    public Event toDomain(EventCreateRequestDto eventCreateRequestDto) {
        return null;
    }
}
