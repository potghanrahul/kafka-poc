package com.code.kafka.producer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka")
@Data
public class KafkaConfigProperties {

    private String bootstrapServer;
    private String acks;
    private String deliveryTimeoutMs;
    private String lingerMs;
    private String requestTimeoutMs;
    private String enableIdempotence;
    private String maxInFlightRequestsPerConnection;
    private TopicConfig topic;

    @Data
    public static class TopicConfig {
        private String name;
        private Integer partitions;
        private Integer replicas;
        private String minInsyncReplicas;
    }
}
