package ru.pas_zhukov.eventmanager.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventSender {

        private static final Logger logger = LoggerFactory.getLogger(EventSender.class);

        private final KafkaTemplate<Long, EventChangeMessage> kafkaTemplate;

        public EventSender(KafkaTemplate<Long, EventChangeMessage> kafkaTemplate) {
            this.kafkaTemplate = kafkaTemplate;
        }

        public void sendEvent(EventChangeMessage changeEvent) {
            logger.info("Sending event: event={}", changeEvent);
            var result = kafkaTemplate.send(
                    "events-topic",
                    changeEvent.getEventId(),
                    changeEvent
            );

            result.thenAccept(sendResult -> {
                logger.info("Send successful");
            });
        }

}
