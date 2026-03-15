package com.code.kafka.producer.service;

import com.code.kafka.core.dto.ProductDto;
import com.code.kafka.core.pojo.ProductDetail;
import com.code.kafka.core.service.ProducerService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ProducerServiceImpl implements ProducerService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String name;

//    @Override
    public String produce(ProductDto productDto) {
        String productId = UUID.randomUUID().toString();

        ProductDetail productDetail = ProductDetail.builder()
                .productId(productId)
                .name(productDto.getName())
                .description(productDto.getDescription())
                .quantity(productDto.getQuantity())
                .price(productDto.getPrice())
                .build();

        CompletableFuture<SendResult<String, Object>> send = kafkaTemplate.send(name, productId, productDetail);

        send.whenCompleteAsync((sendResult, e) -> {
            if (e != null) {
                logger.info("Exception message : {}", e.getMessage());
            } else {
                ProducerRecord<String, Object> producerRecord = sendResult.getProducerRecord();
                logger.info("Topic : {}", producerRecord.topic());
                logger.info("Key : {}", producerRecord.key());
                logger.info("Value : {}", producerRecord.value());
                logger.info("Headers : {}", producerRecord.headers());
                logger.info("Partition : {}", producerRecord.partition());
                logger.info("Timestamp : {}", producerRecord.timestamp());

                RecordMetadata recordMetadata = sendResult.getRecordMetadata();
                logger.info("Timestamp : {}", recordMetadata.timestamp());
                logger.info("HasTimestamp : {}", recordMetadata.hasTimestamp());
                logger.info("Partition : {}", recordMetadata.partition());
                logger.info("Topic : {}", recordMetadata.topic());
                logger.info("Offset : {}", recordMetadata.offset());
                logger.info("SerializedKeySize : {}", recordMetadata.serializedKeySize());
                logger.info("SerializedValueSize : {}", recordMetadata.serializedValueSize());
            }
        });

        return productId;
    }
}
