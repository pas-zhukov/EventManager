package ru.pas_zhukov.eventmanager.schedule;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.pas_zhukov.eventmanager.entity.EventEntity;
import ru.pas_zhukov.eventmanager.entity.RegistrationEntity;
import ru.pas_zhukov.eventmanager.kafka.EventChangeMessage;
import ru.pas_zhukov.eventmanager.kafka.EventSender;
import ru.pas_zhukov.eventmanager.kafka.MessageType;
import ru.pas_zhukov.eventmanager.model.EventStatus;
import ru.pas_zhukov.eventmanager.repository.EventRepository;

import java.util.List;

@Component
public class EventStatusUpdater {

    private final static Logger logger = LoggerFactory.getLogger(EventStatusUpdater.class);
    private final EventRepository eventRepository;
    private final EventSender eventSender;

    public EventStatusUpdater(EventRepository eventRepository, EventSender eventSender) {
        this.eventRepository = eventRepository;
        this.eventSender = eventSender;
    }

    @Scheduled(cron = "${event.stats.cron}")
    public void updateEventStatuses() {
        logger.info("EventStatusUpdater started");

        // найти мероприятия, которые уже начались, но статус WAIT_START
        List<EventEntity> startedEvents = eventRepository.findAllStartedEventsByStatus(EventStatus.WAIT_START);
        if (!startedEvents.isEmpty()) {
            eventRepository.changeEventsStatus(startedEvents.stream().mapToLong(EventEntity::getId).boxed().toList(), EventStatus.STARTED);
        }
        startedEvents.forEach(eventEntity -> {
            eventSender.sendEvent(
                    EventChangeMessage.builder()
                            .withMessageType(MessageType.UPDATED)
                            .withEventId(eventEntity.getId())
                            .withOwnerId(eventEntity.getOwner().getId())
                            .withUsersIds(eventEntity.getRegistrations().stream().mapToLong(RegistrationEntity::getId).boxed().toList())
                            .withStatusChange(EventStatus.WAIT_START, EventStatus.STARTED)
                            .build()
            );
        });

        // найти мероприятия, у которых время окончилось, но статус STARTED
        List<EventEntity> endedEvents = eventRepository.findAllFinishedEventsByStatus(EventStatus.STARTED);
        if (!endedEvents.isEmpty()) {
            eventRepository.changeEventsStatus(endedEvents.stream().mapToLong(EventEntity::getId).boxed().toList(), EventStatus.FINISHED);
        }
        endedEvents.forEach(eventEntity -> {
            eventSender.sendEvent(
                    EventChangeMessage.builder()
                            .withMessageType(MessageType.UPDATED)
                            .withEventId(eventEntity.getId())
                            .withOwnerId(eventEntity.getOwner().getId())
                            .withUsersIds(eventEntity.getRegistrations().stream().mapToLong(RegistrationEntity::getId).boxed().toList())
                            .withStatusChange(EventStatus.STARTED, EventStatus.FINISHED)
                            .build()
            );
        });
    }
}
