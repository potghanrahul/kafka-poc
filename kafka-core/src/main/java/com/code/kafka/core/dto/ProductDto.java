package com.code.kafka.core.dto;

import lombok.Data;

@Data
public class ProductDto {
    private String name;
    private String description;
    private Long quantity;
    private Double price;
}
