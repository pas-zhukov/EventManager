package ru.pas_zhukov.eventmanager.schedule;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
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
        logger.info("EventStatusScheduledUpdater started");

        // найти мероприятия, которые уже начались, но статус WAIT_START
        List<EventEntity> startedEvents = eventRepository.findAllByStatusIs(EventStatus.WAIT_START);
//        startedEvents.forEach(event -> eventRepository.changeEventStatusById(event.getId(), EventStatus.STARTED));

        // найти мероприятия, у которых время окончилось, но статус STARTED
        List<EventEntity> endedEvents = eventRepository.findAllByStatusIs(EventStatus.STARTED);
//        endedEvents.forEach(event -> eventRepository.changeEventStatusById(event.getId(), EventStatus.FINISHED));
    }
}
