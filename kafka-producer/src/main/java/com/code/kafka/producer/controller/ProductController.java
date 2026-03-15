package com.code.kafka.producer.controller;

import com.code.kafka.core.dto.ProductDto;
import com.code.kafka.core.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Autowired
    private ProducerService producerService;

    @PostMapping("/product")
    public String pushProductDetail(@RequestBody ProductDto productDto) {
        return producerService.produce(productDto);
    }

}
