package com.codewithmosh.store.orders;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemDto {
    private OrderProductDto product;
    private int quantity;
    private BigDecimal totalPrice;
}
