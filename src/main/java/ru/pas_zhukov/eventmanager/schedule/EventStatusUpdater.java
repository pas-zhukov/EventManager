package ru.pas_zhukov.eventmanager.schedule;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.pas_zhukov.eventmanager.entity.EventEntity;
import ru.pas_zhukov.eventmanager.model.EventStatus;
import ru.pas_zhukov.eventmanager.repository.EventRepository;

import java.util.List;

@Component
public class EventStatusUpdater {

    private final static Logger logger = LoggerFactory.getLogger(EventStatusUpdater.class);
    private final EventRepository eventRepository;

    public EventStatusUpdater(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Scheduled(cron = "${event.stats.cron}")
    public void updateEventStatuses() {
        logger.info("EventStatusUpdater started");

        // найти мероприятия, которые уже начались, но статус WAIT_START
        List<EventEntity> startedEvents = eventRepository.findAllByStatusIs(EventStatus.WAIT_START);
        if (!startedEvents.isEmpty()) {
            eventRepository.changeEventsStatus(startedEvents.stream().mapToLong(EventEntity::getId).boxed().toList(), EventStatus.STARTED);
        }

        // найти мероприятия, у которых время окончилось, но статус STARTED
        List<EventEntity> endedEvents = eventRepository.findAllByStatusIs(EventStatus.STARTED);
        if (!endedEvents.isEmpty()) {
            eventRepository.changeEventsStatus(endedEvents.stream().mapToLong(EventEntity::getId).boxed().toList(), EventStatus.FINISHED);
        }
    }
}
