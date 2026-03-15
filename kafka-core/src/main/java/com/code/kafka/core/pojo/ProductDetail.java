package com.code.kafka.core.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDetail {
    private String productId;
    private String name;
    private String description;
    private Long quantity;
    private Double price;
}
