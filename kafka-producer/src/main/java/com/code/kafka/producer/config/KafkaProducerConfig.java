package com.code.kafka.producer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;
import static org.apache.kafka.common.config.TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG;

@Configuration
@EnableConfigurationProperties({KafkaProducerProperties.class})
public class KafkaProducerConfig {

    @Autowired
    private KafkaProducerProperties producerConfig;

    @Bean
    public NewTopic newTopic() {
        return TopicBuilder.name(producerConfig.getTopic().getName())
                .partitions(producerConfig.getTopic().getPartitions())
                .replicas(producerConfig.getTopic().getReplicas())
                .configs(Map.of(MIN_IN_SYNC_REPLICAS_CONFIG, producerConfig.getTopic().getMinInsyncReplicas()))
                .build();
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(BOOTSTRAP_SERVERS_CONFIG, producerConfig.getBootstrapServer());
        config.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);


        config.put(DELIVERY_TIMEOUT_MS_CONFIG, producerConfig.getDeliveryTimeoutMs());
        config.put(LINGER_MS_CONFIG, producerConfig.getLingerMs());
        config.put(REQUEST_TIMEOUT_MS_CONFIG, producerConfig.getRequestTimeoutMs());

        config.put(ENABLE_IDEMPOTENCE_CONFIG, producerConfig.getEnableIdempotence());
        //If we are enabling idempotence manually then for following 3 configs we should config default values, otherwise it will throw exception at runtime
        config.put(ACKS_CONFIG, producerConfig.getAcks());
//        config.put(RETRIES_CONFIG, Integer.MAX_VALUE);
        config.put(MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, producerConfig.getMaxInFlightRequestsPerConnection());

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
