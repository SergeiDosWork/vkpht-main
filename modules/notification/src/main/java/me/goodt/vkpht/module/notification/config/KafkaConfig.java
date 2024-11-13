package me.goodt.vkpht.module.notification.config;

import lombok.Getter;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

import me.goodt.vkpht.module.notification.api.dto.kafka.NoticeDto;

@Getter
@Configuration
public class KafkaConfig {
    @Value("${appConfig.kafka.host}")
    private String host;

    @Value("${appConfig.kafka.topic}")
    private String topic;

    @Value("${appConfig.kafka.subtype}")
    private String subtype;

    @Value("${appConfig.kafka.type}")
    private String type;

    @Value("${appConfig.kafka.initiatorKeycloakId}")
    private String initiatorKeycloakId;

    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    public ProducerFactory<Long, NoticeDto> producerStarshipFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<Long, NoticeDto> kafkaTemplate() {
        KafkaTemplate<Long, NoticeDto> template = new KafkaTemplate<>(producerStarshipFactory());
        template.setMessageConverter(new StringJsonMessageConverter());
        return template;
    }
}
