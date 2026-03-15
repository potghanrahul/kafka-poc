package com.code.kafka.producer.config;

import jakarta.annotation.PostConstruct;
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
@EnableConfigurationProperties({KafkaConfigProperties.class})
public class KafkaProducerConfig {

    @Autowired
    KafkaConfigProperties kafkaConfigProperties;

    @Bean
    public NewTopic newTopic() {
        return TopicBuilder.name(kafkaConfigProperties.getTopic().getName())
                .partitions(kafkaConfigProperties.getTopic().getPartitions())
                .replicas(kafkaConfigProperties.getTopic().getReplicas())
                .configs(Map.of(MIN_IN_SYNC_REPLICAS_CONFIG, kafkaConfigProperties.getTopic().getMinInsyncReplicas()))
                .build();
    }

    @PostConstruct
    public void test() {
        System.out.println("KafkaProducerConfig.test");
        System.out.println("kafkaConfigProperties = " + kafkaConfigProperties);
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(BOOTSTRAP_SERVERS_CONFIG, kafkaConfigProperties.getBootstrapServer());
        config.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);


        config.put(DELIVERY_TIMEOUT_MS_CONFIG, kafkaConfigProperties.getDeliveryTimeoutMs());
        config.put(LINGER_MS_CONFIG, kafkaConfigProperties.getLingerMs());
        config.put(REQUEST_TIMEOUT_MS_CONFIG, kafkaConfigProperties.getRequestTimeoutMs());

        config.put(ENABLE_IDEMPOTENCE_CONFIG, kafkaConfigProperties.getEnableIdempotence());
        //If we are enabling idempotence manually then for following 3 configs we should config default values, otherwise it will throw exception at runtime
        config.put(ACKS_CONFIG, kafkaConfigProperties.getAcks());
//        config.put(RETRIES_CONFIG, Integer.MAX_VALUE);
        config.put(MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, kafkaConfigProperties.getMaxInFlightRequestsPerConnection());

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
