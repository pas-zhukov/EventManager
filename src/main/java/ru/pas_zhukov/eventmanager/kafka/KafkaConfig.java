package ru.pas_zhukov.eventmanager.kafka;

import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;


@Configuration
public class KafkaConfig {
    @Bean
    public KafkaTemplate<Long, EventChangeMessage> kafkaTemplate(
            KafkaProperties kafkaProperties
    ) {
        var props = kafkaProperties.buildProducerProperties(
                new DefaultSslBundleRegistry()
        );

        props.put("key.serializer", LongSerializer.class.getName());
        props.put("value.serializer", JsonSerializer.class.getName());

        ProducerFactory<Long, EventChangeMessage> producerFactory = new
                DefaultKafkaProducerFactory<>(props);

        return new KafkaTemplate<>(producerFactory);
    }
}
