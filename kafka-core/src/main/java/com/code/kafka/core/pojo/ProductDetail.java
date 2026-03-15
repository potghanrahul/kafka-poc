package com.code.kafka.core.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDetail {
    private String productId;
    private String name;
    private String description;
    private Long quantity;
    private Double price;
}
