package com.codewithmosh.store.orders;

import java.math.BigDecimal;

import lombok.Data;


@Data
public class OrderProductDto {
    private Long id;
    private String name;
    private BigDecimal price;
}
