package com.code.kafka.core.service;

import com.code.kafka.core.dto.ProductDto;

public interface ProducerService {
    String produce(ProductDto productDto);
}
