package com.codewithmosh.store.products;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Byte categoryId;

}
